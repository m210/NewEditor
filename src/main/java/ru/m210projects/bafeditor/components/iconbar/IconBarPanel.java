package ru.m210projects.bafeditor.components.iconbar;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.components.RadiusButton;

import javax.swing.*;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class IconBarPanel extends JPanel {

    public IconBarPanel() {
        setLayout(new GridLayoutManager(5, 1));

        for (int i = 0; i < 5; i++) {
            add(new RadiusButton("" + i), new GridConstraints(i, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        }

        this.setOpaque(false);
    }
}
