package ru.m210projects.bafeditor.backend.filehandler;

public class ResourceEntry extends URLEntry {
    public ResourceEntry(String path) {
        super(ResourceEntry.class.getResource(path.startsWith("/") ? path : "/" + path));
    }
}
