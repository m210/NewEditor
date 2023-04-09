package ru.m210projects.bafeditor.backend.filehandler.grp;

import ru.m210projects.bafeditor.backend.StreamUtils;
import ru.m210projects.bafeditor.backend.filehandler.Group;
import ru.m210projects.bafeditor.backend.filehandler.InputStreamProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GrpFile implements Group<GrpEntry> {

    static final GrpEntry DUMMY_ENTRY = new GrpEntry(() -> new ByteArrayInputStream(new byte[0]), "dummy", -1, 0);
    private static final String GRP_HEADER = "KenSilverman";
    private final Map<String, GrpEntry> entries;
    private final String name;
    public GrpFile(String name) {
        this.entries = new LinkedHashMap<>();
        this.name = name;
    }

    public GrpFile(String name, InputStreamProvider provider) throws IOException {
        this.name = name;
        try (InputStream is = new BufferedInputStream(provider.newInputStream())) {
            String header = StreamUtils.readString(is, 12);
            if (header.compareTo(GRP_HEADER) != 0) {
                throw new RuntimeException("GRP header corrupted");
            }
            int numFiles = StreamUtils.readInt(is);
            int headerSize = (numFiles + 1) << 4;

            this.entries = new LinkedHashMap<>(numFiles);
            if (numFiles != 0) {
                int offset = headerSize;
                for (int i = 0; i < numFiles; i++) {
                    String fileName = StreamUtils.readString(is, 12);
                    int size = StreamUtils.readInt(is);
                    GrpEntry entry = new GrpEntry(provider, fileName, offset, size);
                    entries.put(fileName.toUpperCase(), entry);
                    offset += size;
                }
            }
        }
    }

    public GrpEntry addEntry(String name, byte[] data) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(data, "data");

        GrpEntry entry;
        synchronized (this) {
            entry = new GrpEntry(() -> new ByteArrayInputStream(data), name, -1, data.length);
            entries.put(name.toUpperCase(), entry);
        }
        return entry;
    }

    public GrpEntry removeEntry(String name) {
        Objects.requireNonNull(name, "name");
        GrpEntry entry;
        synchronized (this) {
            entry = entries.remove(name.toUpperCase());
        }
        return entry;
    }

    public GrpEntry getEntry(String name) {
        Objects.requireNonNull(name, "name");
        GrpEntry entry;
        synchronized (this) {
            entry = entries.getOrDefault(name.toUpperCase(), DUMMY_ENTRY);
        }
        return entry;
    }

    @Override
    public synchronized int getSize() {
        return entries.size();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public synchronized List<GrpEntry> getEntries() {
        return new ArrayList<>(entries.values());
    }

    public boolean save(Path savePath) {
        try (OutputStream os = Files.newOutputStream(savePath)) {
            os.write(GRP_HEADER.getBytes(StandardCharsets.UTF_8));
            StreamUtils.writeInt(os, getSize());
            List<GrpEntry> files = getEntries();

            final int maxNameLength = 12;
            byte[] tmpBuf = new byte[8192];
            for (GrpEntry entry : files) {
                String name = entry.getName();
                Arrays.fill(tmpBuf, 0, maxNameLength, (byte) 0);
                System.arraycopy(name.getBytes(StandardCharsets.UTF_8), 0, tmpBuf, 0, Math.min(name.length(), maxNameLength));
                os.write(tmpBuf, 0, maxNameLength);
                StreamUtils.writeInt(os, entry.getSize());
            }

            for (GrpEntry entry : files) {
                try (InputStream is = entry.getInputStream()) {
                    while (is.available() != 0) {
                        int len = is.read(tmpBuf);
                        os.write(tmpBuf, 0, len);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        StringJoiner files = new StringJoiner(", ");
        for (GrpEntry e : entries.values()) {
            files.add(e.getName());
        }
        return "GrpFile{" +
                "entries=" + files +
                '}';
    }
}