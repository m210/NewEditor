package ru.m210projects.bafeditor.backend.filehandler;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static ru.m210projects.bafeditor.backend.filehandler.fs.Directory.DUMMY_ENTRY;

public class EntryGroup implements Group<Entry> {

    protected final Map<String, Entry> entries = new HashMap<>();
    protected final String name;

    public EntryGroup(String name) {
        this.name = name;
    }

    public void addAll(Collection<Entry> entries) {
        for(Entry entry : entries) {
            add(entry);
        }
    }

    public void add(Entry entry) {
        if (entry.exists()) {
            entries.put(entry.getName(), entry);
        } else {
            System.err.printf("Warning: entry \"%s\" is not exists", entry.getName());
        }
    }

    public boolean remove(Entry entry) {
        return entries.remove(entry.getName(), entry);
    }

    @Override
    public synchronized List<Entry> getEntries() {
        return new ArrayList<>(entries.values());
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public String getName() {
        return name;
    }

    @NotNull
    public Entry getEntry(String name) {
        synchronized (this) {
            return entries.getOrDefault(name.toUpperCase(), DUMMY_ENTRY);
        }
    }
}
