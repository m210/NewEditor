package ru.m210projects.bafeditor.backend.filehandler;

import java.util.Iterator;
import java.util.List;

public interface Group<T extends Entry> extends Iterable<T> {

    List<T> getEntries();
    int getSize();
    String getName();
    T getEntry(String name);

    default Iterator<T> iterator() {
        return getEntries().iterator();
    }
}
