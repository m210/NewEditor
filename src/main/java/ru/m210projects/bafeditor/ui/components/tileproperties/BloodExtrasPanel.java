package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.Controller;
import ru.m210projects.bafeditor.ui.components.AngelChangerPanel;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.PanelUI;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class BloodExtrasPanel extends JPanel {

    private AngelChangerPanel angelChangerPanel;

    public BloodExtrasPanel(Controller controller) {
        super(false);
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(2, 2));
        add(new RadiusButton("Voxel", null), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new RadiusButton("ID", null), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new RadiusButton("None", null), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(angelChangerPanel = new AngelChangerPanel(controller::onViewAngleChanged), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.setOpaque(false);
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }

    public void onTileSelected(int tile) {
        angelChangerPanel.resetAngle();
    }
}
