package ru.m210projects.bafeditor;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.backend.filehandler.Directory;
import ru.m210projects.bafeditor.components.RadiusButton;
import ru.m210projects.bafeditor.components.ShadowUtils;
import ru.m210projects.bafeditor.components.filelist.FileListPanel;
import ru.m210projects.bafeditor.components.iconbar.IconBarPanel;
import ru.m210projects.bafeditor.components.tileproperties.TilePropertiesTree;
import ru.m210projects.bafeditor.components.TileViewer;
import ru.m210projects.bafeditor.models.TileContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Paths;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class MainForm extends JFrame {

    public static final String APP_VERSION = "v2.5";
    public static final String APP_NAME = "Build ART Files Editor";

    private JPanel root;
    private JPanel iconBar;
    private JPanel fileList;
    private JPanel tileBrowser;
    private JPanel tileViewer;
    private JPanel tilePropButtons;
    private JPanel tilePropTree;
    private JPanel tilePanel;

    public MainForm() {
        setContentPane(root);

        initIconBar();
        initFileList();
        initTileButtons();
        initTilePropTree();
        initTileBrowser();
        initTileViewer();
        root.setBackground(Color.WHITE);

        setTitle(APP_NAME);
//        setIconImage(Toolkit.getDefaultToolkit().getImage(logoURL));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void initTileBrowser() {
        TileViewer tiles = new TileViewer() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Rectangle view = this.getVisibleRect();
                ShadowUtils.drawRectShadow(g, 0, 0, view.width - 1, view.height - 1, true);
            }
        };
        tileBrowser.add(tiles, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, new Dimension(64, 64), null, null, 0, false));
    }

    private void initIconBar() {
        iconBar.add(new IconBarPanel(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private void initFileList() {
        FileListPanel files = new FileListPanel();

        try {
            Directory dir = new Directory(Paths.get("D:\\Temp\\Blood\\"));
            files.updateFileList(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileList.add(files, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private void initTileViewer() {
        tileViewer.add(new TileViewer(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private void initTilePropTree() {
        TilePropertiesTree tree = new TilePropertiesTree();
        tree.setToggleClickCount(1);
        tree.update(new TileContainer());
        tree.setOpaque(false);
        tilePropTree.add(tree, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private void initTileButtons() {
        tilePropButtons.setLayout(new GridLayoutManager(2, 3));
        tilePropButtons.add(new RadiusButton("Fill"), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtons.add(new RadiusButton("Reset position"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtons.add(new RadiusButton("Reset zoom"), new GridConstraints(0, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtons.add(new RadiusButton("Cross"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtons.add(new RadiusButton("Prev. contour"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropButtons.add(new RadiusButton("Next contour"), new GridConstraints(1, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

}
