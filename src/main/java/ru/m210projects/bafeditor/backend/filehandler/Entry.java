package ru.m210projects.bafeditor.backend.filehandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface Entry {

    Entry DUMMY_ENTRY = new Entry() {
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public String getName() {
            return "Dummy";
        }

        @Override
        public String getExtension() {
            return "";
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public boolean exists() {
            return false;
        }
    };

    InputStream getInputStream() throws IOException;

    String getName();

    String getExtension();

    long getSize();

    boolean exists();

    default int readBuffer(ByteBuffer buffer) {
        int len = 0;
        int remaining = buffer.remaining();
        long size = getSize();
        if (exists() && size > 0) {
            byte[] data = new byte[8192];
            try (InputStream is = getInputStream()) {
                while (remaining > 0) {
                    int l = is.read(data, 0, Math.min(remaining, 8192));
                    buffer.put(data, 0, l);
                    remaining -= l;
                    len += l;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return len;
    }

    default byte[] getBytes() {
        long size = getSize();
        if (exists() && size > 0) {
            byte[] data = new byte[(int) size];
            try (InputStream is = getInputStream()) {
                int len = is.read(data);
                if (len == size) {
                    return data;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}