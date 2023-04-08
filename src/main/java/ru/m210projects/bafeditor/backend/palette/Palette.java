package ru.m210projects.bafeditor.backend.palette;

import ru.m210projects.bafeditor.backend.StreamUtils;
import ru.m210projects.bafeditor.backend.filehandler.Entry;

import java.io.IOException;
import java.io.InputStream;

public class Palette {
    private final byte[] palette;


    public Palette(Entry entry) throws IOException {
        try (InputStream is = entry.getInputStream()) {
            System.out.println("Loading palettes");
            this.palette = StreamUtils.readBytes(is, 768);
        }
    }

    public int getRed(int index) {
        return palette[3 * index] & 0xFF;
    }

    public int getGreen(int index) {
        return palette[3 * index + 1] & 0xFF;
    }

    public int getBlue(int index) {
        return palette[3 * index + 2] & 0xFF;
    }
}
