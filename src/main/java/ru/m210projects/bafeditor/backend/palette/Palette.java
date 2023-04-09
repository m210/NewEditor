package ru.m210projects.bafeditor.backend.palette;

import ru.m210projects.bafeditor.backend.StreamUtils;
import ru.m210projects.bafeditor.backend.filehandler.Entry;

import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.InputStream;

public class Palette {

    private static final int COLORS = 256;
    private IndexColorModel model;

    public Palette(Entry entry, Format format) throws IOException {
        try (InputStream is = entry.getInputStream()) {
            System.out.println("Loading palettes");
            byte[] palette;

            switch (format) {
                case BUILD:
                    palette = StreamUtils.readBytes(is, 768);
                    for (int i = 0; i < 768; i++) {
                        palette[i] <<= 2;
                    }
                    break;
                case MICROSOFT:
                    String signature = StreamUtils.readString(is, 4);
                    if (signature.equals("RIFF")) {
                        throw new RuntimeException("Wrong signature: " + signature);
                    }

                    StreamUtils.skip(is, 4); // length
                    String palSign = StreamUtils.readString(is, 4);
                    if (palSign.equals("PAL ")) {
                        throw new RuntimeException("Wrong PAL signature: " + palSign);
                    }

                    String data = StreamUtils.readString(is, 4);
                    if (data.equals("DATA")) {
                        throw new RuntimeException("Wrong DATA header: " + data);
                    }

                    StreamUtils.skip(is, 4); // chunk size
                    if (StreamUtils.readShort(is) != 0x0300) {
                        throw new RuntimeException("Wrong chunk");
                    }

                    int colors = StreamUtils.readShort(is);
                    if (colors != 256) {
                        throw new RuntimeException("Unsupported number of colors");
                    }

                    palette = new byte[768];
                    for (int i = 0; i < colors; i++) {
                        palette[3 * i] = (byte) is.read(); // red
                        palette[3 * i + 1] = (byte) is.read(); // green
                        palette[3 * i + 2] = (byte) is.read(); // blue
                        StreamUtils.skip(is, 1); // flags
                    }
                    break;
                case PHOTOSHOP:
                    palette = StreamUtils.readBytes(is, 768);
                    break;
                default:
                    throw new UnsupportedOperationException("Wrong palette format");
            }

            this.model = new IndexColorModel(8, COLORS, palette, 0, false, 255);
        }
    }

    public IndexColorModel getModel() {
        return model;
    }
}
