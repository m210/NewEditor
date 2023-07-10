package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class TreeGroup extends JPanel {

    private final JLabel status;

    public TreeGroup(String name) {
        super(false);
        setBorder(new EmptyBorder(TilePropertiesTree.GROUP_HEIGHT / 2, 0, TilePropertiesTree.GROUP_HEIGHT / 2, 10));
        setLayout(new GridLayoutManager(1, 2));
        add(new JLabel(name), new GridConstraints(0, 0, 1, 1, ANCHOR_CENTER, FILL_HORIZONTAL, SIZEPOLICY_CAN_SHRINK | SIZEPOLICY_CAN_GROW, SIZEPOLICY_FIXED, null, null, null));
        add((status = new JLabel()), new GridConstraints(0, 1, 1, 1, ANCHOR_EAST, FILL_NONE, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }

    public void setExpanded(boolean expanded) {
        status.setText(expanded ? "+" : "-");
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }
}
