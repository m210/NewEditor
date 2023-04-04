package ru.m210projects.bafeditor.components.filelist;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.backend.filehandler.Directory;
import ru.m210projects.bafeditor.backend.filehandler.FileEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static ru.m210projects.bafeditor.backend.filehandler.Directory.DUMMY_ENTRY;

public class FileListPanel extends JPanel {
    private final DefaultListModel<String> listModel;

    public FileListPanel() {
        setLayout(new GridLayoutManager(1, 1));

        listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        add(list, new GridConstraints(0, 0, 1, 1, ANCHOR_CENTER, FILL_BOTH, SIZEPOLICY_CAN_GROW, SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null));

        this.setOpaque(false);
    }

    public void updateFileList(Directory directory) {
        listModel.removeAllElements();
        List<FileEntry> files = directory.getEntries();
        int i = 0;
        for (FileEntry entry : files) {
            if (entry.getExtension().equals("art")) {
                listModel.addElement(entry.getName());
                i++;
            }
        }

        if (i != 0) {
            return;
        }

        listModel.addElement(DUMMY_ENTRY.getName());
    }
}
