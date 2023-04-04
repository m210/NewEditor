package ru.m210projects.bafeditor.backend.filehandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

public class Directory implements Group<FileEntry> {
    public static final FileEntry DUMMY_ENTRY = new FileEntry(Paths.get("DUMMY"), "dummy", -1) {
        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }
    };
    public static final Directory DUMMY_DIRECTORY = new Directory() {
        @Override
        public List<FileEntry> getEntries() {
            return new ArrayList<>();
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public String getName() {
            return "dummy";
        }

        @Override
        public @NotNull FileEntry getEntry(String name) {
            return DUMMY_ENTRY;
        }
    };

    private final Map<String, FileEntry> entries = new HashMap<>();
    private final Map<String, Directory> directories = new HashMap<>();
    private final Path path;

    private Directory() {
        this.path = DUMMY_ENTRY.getPath();
    }

    public Directory(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            this.path = dir;
            for (Path path : stream) {
                FileEntry entry = getEntry(path);
                if (entry.exists()) {
                    entries.put(entry.getName().toUpperCase(), entry);
                }
            }
        }
    }

    @Override
    public synchronized int getSize() {
        return entries.size();
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public synchronized List<FileEntry> getEntries() {
        return new ArrayList<>(entries.values());
    }

    @Nullable
    public Path getParent() {
        return path.getParent();
    }

    @NotNull
    public FileEntry getEntry(String name) {
        Objects.requireNonNull(name, "name");
        FileEntry entry;
        synchronized (this) {
            entry = entries.getOrDefault(name.toUpperCase(), DUMMY_ENTRY);
            if (entry.isDirectory()) {
                addDirectory(entry);
            }
        }
        return entry;
    }

    @NotNull
    public synchronized FileEntry getEntry(String... relativePath) {
        Directory dir = this;
        FileEntry result = DUMMY_ENTRY;
        for (String p : relativePath) {
            FileEntry entry = dir.getEntry(p);
            if (!entry.exists()) {
                return DUMMY_ENTRY;
            }

            if (entry.isDirectory()) {
                dir = dir.addDirectory(entry);
            }
            result = entry;
        }
        return result;
    }

    @NotNull
    public Directory getDirectory(FileEntry dirEntry) {
        Directory directory = DUMMY_DIRECTORY;
        if (dirEntry != null && dirEntry.isDirectory()) {
            try {
                Path relPath = this.path.relativize(dirEntry.getPath());
                Directory dir = this;
                for (Path p : relPath) {
                    Map<String, Directory> directories = dir.directories;
                    String key = p.toString().toUpperCase();

                    // if key is not exists and directory contains this entry, add entry to map
                    if (!directories.containsKey(key) && dir.entries.containsValue(dirEntry)) {
                        directories.put(key, new Directory(dirEntry.getPath()));
                    }
                    dir = directories.get(key);
                }
                directory = dir;
            } catch (IOException ignored) {
                return DUMMY_DIRECTORY;
            }
        }
        return directory;
    }

    @NotNull
    private Directory addDirectory(FileEntry entry) {
        Directory dir = this;
        try {
            String key = entry.getName().toUpperCase();
            dir = directories.getOrDefault(key, new Directory(entry.getPath()));
            directories.putIfAbsent(key, dir);
        } catch (IOException ignored) {
        }
        return dir;
    }

    @NotNull
    private FileEntry getEntry(Path path) {
        try {
            String name = path.getFileName().toString();
            return new FileEntry(path, name, Files.size(path));
        } catch (NoSuchFileException e) {
            System.err.println(String.format("Path \"%s\" is not found.", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DUMMY_ENTRY;
    }
}
