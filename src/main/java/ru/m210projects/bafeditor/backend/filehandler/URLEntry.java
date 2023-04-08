package ru.m210projects.bafeditor.backend.filehandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLEntry implements Entry {

    private final URL url;
    private final String name;
    private final String extension;
    private final long size;

    public URLEntry(URL url) {
        this.url = url;
        if (url != null) {
            String path = url.getFile();

            if (path.contains(".")) {
                this.extension = path.substring(path.lastIndexOf(".") + 1);
            } else {
                this.extension = "";
            }
            this.name = path.substring(path.lastIndexOf('/') + 1);
            this.size = getFileSize(url);
        } else {
            System.err.println("Warning: URL == null!");
            this.name = this.extension = "";
            this.size = -1;
        }
    }

    private int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return -1;
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (exists()) {
            return url.openStream();
        }
        return new ByteArrayInputStream(new byte[0]);
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
        return size != -1;
    }

    @Override
    public String toString() {
        return "URLEntry{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
