package ru.m210projects.bafeditor.backend.filehandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceEntry implements Entry {

    private final URL url;
    private final String name;
    private final String extension;
    private final long size;


    public ResourceEntry(String path) {
        this.url = ResourceEntry.class.getResource(path);
        if(path.contains(".")) {
            this.extension = path.substring(path.lastIndexOf(".") + 1);
        } else {
            this.extension = "";
        }
        File file = new File(url.getFile());
        this.name = file.getName();
        this.size = file.length();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
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
        return 0;
    }

    @Override
    public boolean exists() {
        return false;
    }
}
