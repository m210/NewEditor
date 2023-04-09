package ru.m210projects.bafeditor.ui;


import com.intellij.uiDesigner.core.GridConstraints;

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

        Controller controller = new Controller();
        View mainView = new View(controller);

        iconBar.add(mainView.getIconBarPanel(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileListHolder.add(mainView.getFileList(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropTreeHolder.add(mainView.getTilePropertiesTree(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tileBrowserHolder.add(mainView.getTileBrowser(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, new Dimension(64, 64), null, null, 0, false));
        tileViewerHolder.add(mainView.getTileViewer(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        root.setBackground(Color.WHITE);

        setTitle(APP_NAME);
//        setIconImage(Toolkit.getDefaultToolkit().getImage(logoURL));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

}
