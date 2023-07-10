package ru.m210projects.bafeditor.ui.components.filelist;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.NotNull;
import ru.m210projects.bafeditor.backend.filehandler.Entry;
import ru.m210projects.bafeditor.backend.filehandler.Group;
import ru.m210projects.bafeditor.ui.models.EntryModel;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class FileListPanel extends JPanel {

    private final FileAdapter fileAdapter;

    public FileListPanel() {
        super(false);
        setLayout(new GridLayoutManager(1, 1));

        fileAdapter = new FileAdapter();
        JList<EntryModel> list = new JList<>(fileAdapter);
        list.addListSelectionListener(fileAdapter);
        add(list, new GridConstraints(0, 0, 1, 1, ANCHOR_CENTER, FILL_BOTH, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, null, new Dimension(150, 50), null));
        setOpaque(false);
    }

    public void setEntryClickListener(onEntryClickListener entryClickListener) {
        fileAdapter.setEntryClickListener(entryClickListener);
    }

    public void updateFileList(@NotNull Group<Entry> group) {
        fileAdapter.update(group);
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }
}
