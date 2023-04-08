package ru.m210projects.bafeditor.backend.tiles;

import ru.m210projects.bafeditor.backend.StreamUtils;
import ru.m210projects.bafeditor.backend.filehandler.Group;
import ru.m210projects.bafeditor.backend.filehandler.InputStreamProvider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtFile implements Group<ArtEntry> {
    static final ArtEntry DUMMY_ENTRY = new ArtEntry(() -> new ByteArrayInputStream(new byte[0]), -1, -1, 0, 0, 0);
    private static final String HEADER = "BUILDART";
    private final int tileStart;
    private final String name;
    private final List<ArtEntry> entries;

    public ArtFile(String name, InputStreamProvider provider) {
        this.name = name;
        List<ArtEntry> entries = null;
        int tileStart = -1;
        boolean valid = false;

        try (InputStream is = new BufferedInputStream(provider.newInputStream())) {
            int version = checkVersion(is);
            if (version == -1) {
                throw new RuntimeException("Unsupported ART file");
            }

            StreamUtils.readInt(is); //numtiles
            tileStart = StreamUtils.readInt(is);
            int tileEnd = StreamUtils.readInt(is);
            int numTiles = tileEnd - tileStart + 1;
            entries = new ArrayList<>(numTiles);

            int[] sizx = new int[numTiles];
            int[] sizy = new int[numTiles];
            int[] flags = new int[numTiles];

            for (int i = 0; i < numTiles; i++) {
                sizx[i] = StreamUtils.readShort(is);
            }

            for (int i = 0; i < numTiles; i++) {
                sizy[i] = StreamUtils.readShort(is);
            }

            for (int i = 0; i < numTiles; i++) {
                flags[i] = StreamUtils.readInt(is);
            }

            int num = tileStart;
            int offset = 4 + 4 + 4 + 4 + ((tileEnd - tileStart + 1) << 3);
            for (int i = 0; i < numTiles; i++) {
                int width = sizx[i];
                int height = sizy[i];
                ArtEntry entry = new ArtEntry(provider, num++, offset, width, height, flags[i]);
                entries.add(entry);
                offset += entry.getSize();
            }
        } catch(Exception e) {
            System.err.println("Can't load ART file: " + e.getMessage());
            if(entries == null) {
                entries = Collections.unmodifiableList(new ArrayList<>(0));
            }
        }

        this.entries = entries;
        this.tileStart = tileStart;
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArtEntry getEntry(String fileName) {
        return DUMMY_ENTRY;
    }

    @Override
    public List<ArtEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public ArtEntry getEntry(int tileNum) {
        int index = tileNum - tileStart;
        if(index < 0 || index >= entries.size()) {
            return DUMMY_ENTRY;
        }
        return entries.get(index);
    }

    private int checkVersion(InputStream is) throws IOException {
        int b = is.read();
        if (b == 1) {
            if ((is.read() | is.read() | is.read()) == 0) {
                return 1;
            }
        } else if (b == HEADER.charAt(0)) {
            int c = 1;
            while (c < HEADER.length()) {
                if (HEADER.charAt(c) != is.read()) {
                    break;
                }
                c++;
            }

            if (c == HEADER.length()) {
                return StreamUtils.readInt(is);
            }
        }
        return -1;
    }
}
