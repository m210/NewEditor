package ru.m210projects.bafeditor.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;
import static ru.m210projects.bafeditor.components.tileproperties.TilePropertiesTree.BOTTOM_PADDING;
import static ru.m210projects.bafeditor.components.tileproperties.TilePropertiesTree.TOP_PADDING;

public class BloodExtrasPanel extends JPanel {

    public BloodExtrasPanel() {
        setBorder(new EmptyBorder(TOP_PADDING, 0, BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(2, 2));
        RadiusButton tilesButton = new RadiusButton("Voxel");
        tilesButton.addActionListener(e -> System.out.println("Voxel"));
        add(tilesButton, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("ID"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new RadiusButton("None"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Angle"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }
}
