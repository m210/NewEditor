package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.ui.Controller;
import ru.m210projects.bafeditor.ui.components.AngelChangerPanel;
import ru.m210projects.bafeditor.ui.components.MaterialComboBox;
import ru.m210projects.bafeditor.ui.components.MaterialInputContainer;
import ru.m210projects.bafeditor.ui.components.RadiusButton;
import ru.m210projects.bafeditor.ui.models.BloodData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.PanelUI;

import java.awt.*;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class BloodExtrasPanel extends JPanel {

    private AngelChangerPanel angelChangerPanel;

    public BloodExtrasPanel(Controller controller) {
        super(false);
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(3, 2));

        JComboBox<String> voxel = new JComboBox<>(new String[]{ "Single", "5 view objects", "8 view objects", "Flat", "Voxel", "Spin voxel" });
        add(new MaterialComboBox(voxel), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(new MaterialInputContainer(new JLabel("ID"), null, -1, Integer.MAX_VALUE), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        JComboBox<String> surface = new JComboBox<>(BloodData.SURF_NAMES);
        add(new MaterialComboBox(surface), new GridConstraints(2, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(angelChangerPanel = new AngelChangerPanel(controller::onViewAngleChanged), new GridConstraints(0, 1, 3, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

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
