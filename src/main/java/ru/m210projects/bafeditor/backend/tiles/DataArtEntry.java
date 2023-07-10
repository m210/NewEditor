package ru.m210projects.bafeditor.backend.tiles;

import java.io.ByteArrayInputStream;

public class DataArtEntry extends ArtEntry {
    private final byte[] data;

    public DataArtEntry(byte[] data, int width, int height) {
        super(() -> new ByteArrayInputStream(data), 0, 0, width, height, 0);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean exists() {
        return data != null && data.length != 0;
    }
}
