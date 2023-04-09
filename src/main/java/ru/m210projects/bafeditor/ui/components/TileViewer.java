package ru.m210projects.bafeditor.ui.components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.Controller;

import javax.swing.*;
import java.awt.*;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class TileViewer extends JPanel {

    private final TileCanvas viewer;

    public TileViewer(Controller controller) {
        setLayout(new GridLayoutManager(2, 1));
        viewer = new TileCanvas();
        add(viewer, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, new Dimension(256, 256), null, 0, false));

        JPanel propButtonsHolder = new JPanel();
        propButtonsHolder.setOpaque(false);
        propButtonsHolder.setLayout(new GridLayoutManager(2, 3));
        propButtonsHolder.add(new RadiusButton("Fill", controller::onFillButtonClicked), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Reset position", controller::onResetPositionButtonClicked), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Reset zoom", controller::onResetZoomButtonClicked), new GridConstraints(0, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Cross", controller::onCrossButtonClicked), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Prev. contour", controller::onPrevContourButtonClicked), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Next contour", controller::onNextContourButtonClicked), new GridConstraints(1, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(propButtonsHolder, new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_FIXED, null, null, null, 0, false));
        setOpaque(false);
    }

    public TileCanvas getViewer() {
        return viewer;
    }
}
