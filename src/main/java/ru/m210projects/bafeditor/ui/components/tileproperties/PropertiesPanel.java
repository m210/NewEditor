package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class PropertiesPanel extends JPanel {

    public PropertiesPanel() {
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(4, 2));

        add(new JLabel("Tile width:"), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new JTextField("64"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new JLabel("Tile height:"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new JTextField("128"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new JLabel("Tile checksum:"), new GridConstraints(2, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new JTextField("28684466846"), new GridConstraints(2, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new RadiusButton("X"), new GridConstraints(3, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Y"), new GridConstraints(3, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }
}
