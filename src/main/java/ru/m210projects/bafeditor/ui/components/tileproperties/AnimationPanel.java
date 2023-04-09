package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.Controller;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class AnimationPanel extends JPanel {

    private final RadiusButton animationTrigger;

    public AnimationPanel(Controller controller) {
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(2, 2));
        add(new RadiusButton("Tiles", null), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Forward", null), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("Speed", null), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        animationTrigger = new RadiusButton("Start", controller::onAnimationTriggerClicked);
        add(animationTrigger, new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }

    public RadiusButton getAnimationTrigger() {
        return animationTrigger;
    }
}
