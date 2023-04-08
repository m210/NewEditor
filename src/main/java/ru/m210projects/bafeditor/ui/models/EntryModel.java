package ru.m210projects.bafeditor.ui.models;

import ru.m210projects.bafeditor.backend.filehandler.Entry;

public class EntryModel {

    private final Entry entry;
    private final String name;

    public EntryModel(Entry entry, String name) {
        this.entry = entry;
        this.name = name;
    }

    public Entry getEntry() {
        return entry;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
