package ru.m210projects.bafeditor.ui.components.iconbar;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.plaf.PanelUI;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class IconBarPanel extends JPanel {

    public IconBarPanel() {
        super(false);
        setLayout(new GridLayoutManager(5, 1));

        for (int i = 0; i < 5; i++) {
            add(new RadiusButton("" + i, null), new GridConstraints(i, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        }

        this.setOpaque(false);
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }
}
