package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class AnimationPanel extends JPanel {

    public AnimationPanel() {
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(2, 2));
        RadiusButton tilesButton = new RadiusButton("Tiles");
        tilesButton.addActionListener(e -> System.out.println("Tiles"));
        add(tilesButton, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Forward"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new RadiusButton("Speed"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Stop"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }
}
