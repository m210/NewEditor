package ru.m210projects.bafeditor.backend.filehandler.grp;

import ru.m210projects.bafeditor.backend.filehandler.Entry;
import ru.m210projects.bafeditor.backend.filehandler.EntryInputStream;
import ru.m210projects.bafeditor.backend.filehandler.InputStreamProvider;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GrpEntry implements Entry {
    private final int offset;
    private final int size;
    private final String name;
    private final String extension;
    private final InputStreamProvider provider;

    public GrpEntry(InputStreamProvider provider, String name, int offset, int size) {
        this.provider = provider;
        this.offset = offset;
        this.size = size;
        this.name = name;
        if (name.contains(".")) {
            this.extension = name.substring(name.lastIndexOf(".") + 1);
        } else {
            this.extension = "";
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = provider.newInputStream();
        if (is.skip(offset) != offset) {
            throw new EOFException();
        }
        return new EntryInputStream(is, size);
    }

    @Override
    public long getSize() {
        return size;
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
    public boolean exists() {
        return !this.equals(DUMMY_ENTRY);
    }

    @Override
    public String toString() {
        return "GrpEntry{" +
                "name='" + name + '\'' +
                ", offset=" + offset +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrpEntry)) return false;
        GrpEntry grpEntry = (GrpEntry) o;
        return offset == grpEntry.offset && size == grpEntry.size && Objects.equals(name, grpEntry.name) && Objects.equals(provider, grpEntry.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, size, name, provider);
    }
}