package ru.m210projects.bafeditor.ui.components.filelist;

import ru.m210projects.bafeditor.backend.filehandler.Entry;
import ru.m210projects.bafeditor.backend.filehandler.Group;
import ru.m210projects.bafeditor.ui.models.EntryModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Comparator;
import java.util.List;

import static ru.m210projects.bafeditor.backend.filehandler.Directory.DUMMY_ENTRY;

public class FileAdapter extends DefaultListModel<EntryModel> implements ListSelectionListener {

    private Group<Entry> group;
    private onEntryClickListener entryClickListener;

    public void update(Group<Entry> group) {
        this.group = group;

        removeAllElements();
        List<Entry> files = group.getEntries();
        files.sort(Comparator.comparing(Entry::getName));
        int i = 0;
        for (Entry entry : files) {
            if (entry.getExtension().equals("art")) {
                addElement(new EntryModel(entry, entry.getName()));
                i++;
            }
        }

        if (i != 0) {
            return;
        }

        addElement(new EntryModel(DUMMY_ENTRY, ""));
    }

    public void setEntryClickListener(onEntryClickListener entryClickListener) {
        this.entryClickListener = entryClickListener;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<?> list = (JList<?>) e.getSource();
            int selectedIndex = list.getSelectedIndex();
            if (entryClickListener != null) {
                entryClickListener.onEntryClicked(elementAt(selectedIndex).getEntry());
            }
        }
    }
}
