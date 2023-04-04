package ru.m210projects.bafeditor.backend.filehandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileEntry implements Entry {

    private final Path path;
    private final String name;
    private final String extension;
    private final long size;

    public FileEntry(Path path, String name, long size) {
        this.path = path;
        this.name = name;
        if(name.contains(".")) {
            this.extension = name.substring(name.lastIndexOf(".") + 1);
        } else {
            this.extension = "";
        }
        this.size = size;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean exists() {
        return Files.exists(path);
    }

    @Override
    public String toString() {
        return "FileEntry{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
