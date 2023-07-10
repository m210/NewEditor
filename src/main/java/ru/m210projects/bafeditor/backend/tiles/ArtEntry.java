package ru.m210projects.bafeditor.backend.tiles;

import ru.m210projects.bafeditor.backend.filehandler.Entry;
import ru.m210projects.bafeditor.backend.filehandler.EntryInputStream;
import ru.m210projects.bafeditor.backend.filehandler.InputStreamProvider;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.CRC32;

public class ArtEntry implements Entry {

    public static final int[] POW2LONG = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
            65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728,
            268435456, 536870912, 1073741824, 2147483647 };


    private BufferedImage raster;
    private int num;
    private final int offset;
    private final int width;
    private final int height;
    private final int sizex;
    private final int sizey;
    private int flags;
    private final InputStreamProvider provider;
    private final int size;

    private long checksum;

    public ArtEntry(InputStreamProvider provider, int num, int offset, int width, int height, int flags) {
        this.provider = provider;
        this.num = num;
        this.offset = offset;
        this.width = width;
        int sizex = 15;
        while ((sizex > 1) && (POW2LONG[sizex] > width)) {
            sizex--;
        }
        this.sizex = sizex;
        this.height = height;
        int sizey = 15;
        while ((sizey > 1) && (POW2LONG[sizey] > height)) {
            sizey--;
        }
        this.sizey = sizey;
        this.flags = flags;
        this.size = width * height;
    }

    public InputStream getInputStream() throws IOException {
        InputStream is = provider.newInputStream();
        if (is.skip(offset) != offset) {
            throw new EOFException();
        }
        return new EntryInputStream(is, size);
    }

    public BufferedImage getRaster(IndexColorModel palette) {
        if (raster == null) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, palette);
            CRC32 crc32 = new CRC32();

            int[] tmp = new int[1];
            try (InputStream is = getInputStream()) {
                for (int k = 0; k < getSize(); k++) {
                    int row = (int) Math.floor(k / (double) height);
                    int col = k % height;
                    tmp[0] = is.read();
                    crc32.update(tmp[0]);
                    image.getRaster().setPixel(row, col, tmp);
                }
                checksum = crc32.getValue();
            } catch (IOException e) {
                System.err.println("Failed to load tile " + num);
                System.err.println(e.getMessage());
            }
            raster = image;
        }
        return raster;
    }

    public void setRaster(BufferedImage raster) {
        this.raster = raster;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getExtension() {
        return "";
    }

    @Override
    public boolean exists() {
        return !this.equals(DUMMY_ENTRY);
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSizex() {
        return sizex;
    }

    public int getSizey() {
        return sizey;
    }

    public long getSize() {
        return size;
    }

    public byte getOffsetX() {
        return (byte) ((flags >> 8) & 0xFF);
    }

    public byte getOffsetY() {
        return (byte) ((flags >> 16) & 0xFF);
    }

    public void setOffset(byte x, byte y) {
        flags &= ~0x00FFFF00;
        flags |= ((x & 0xFF) << 8) | ((y & 0xFF) << 16);
    }

    public int getFrames() {
        return flags & 0x3F;
    }

    public int getSpeed() {
        return (flags >> 24) & 15;
    }

    public AnimType getType() {
        return AnimType.get(flags);
    }

    // Blood extra bits
    public ViewType getViewType() {
        return ViewType.get(flags);
    }

    public long getChecksum() {
        return checksum;
    }

    @Override
    public String toString() {
        return "ArtEntry{" + "num=" + num + ", offset=" + offset + ", width=" + width + ", height=" + height + ", flags=" + flags + ", size=" + size + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtEntry)) return false;
        ArtEntry artEntry = (ArtEntry) o;
        return num == artEntry.num && offset == artEntry.offset && width == artEntry.width && height == artEntry.height && flags == artEntry.flags && size == artEntry.size && Objects.equals(provider, artEntry.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, offset, width, height, flags, provider, size);
    }
}
