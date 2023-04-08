package ru.m210projects.bafeditor.ui;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.ui.components.RadiusButton;
import ru.m210projects.bafeditor.ui.components.iconbar.IconBarPanel;

import javax.swing.*;
import java.awt.*;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class MainFrame extends JFrame {

    public static final String APP_VERSION = "v2.5";
    public static final String APP_NAME = "Build ART Files Editor";

    private JPanel root;
    private JPanel iconBar;
    private JPanel fileListHolder;
    private JPanel tileBrowserHolder;
    private JPanel tileViewerHolder;
    private JPanel tilePropButtonsHolder;
    private JPanel tilePropTreeHolder;
    private JPanel tilePanelHolder;


    public MainFrame() {
        setContentPane(root);

        View mainController = new View();

        initIconBar();
        fileListHolder.add(mainController.getFileListPanel(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        initTileButtons();
        tilePropTreeHolder.add(mainController.getTilePropertiesTree(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tileBrowserHolder.add(mainController.getTileBrowser(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, new Dimension(64, 64), null, null, 0, false));
        tileViewerHolder.add(mainController.getTileViewer(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        root.setBackground(Color.WHITE);

        setTitle(APP_NAME);
//        setIconImage(Toolkit.getDefaultToolkit().getImage(logoURL));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void initIconBar() {
        iconBar.add(new IconBarPanel(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private void initTileButtons() {
        tilePropButtonsHolder.setLayout(new GridLayoutManager(2, 3));
        tilePropButtonsHolder.add(new RadiusButton("Fill"), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtonsHolder.add(new RadiusButton("Reset position"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtonsHolder.add(new RadiusButton("Reset zoom"), new GridConstraints(0, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtonsHolder.add(new RadiusButton("Cross"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtonsHolder.add(new RadiusButton("Prev. contour"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtonsHolder.add(new RadiusButton("Next contour"), new GridConstraints(1, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

}
