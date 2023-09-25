package ru.m210projects.bafeditor.ui;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.ui.components.ModernScrollPane;
import ru.m210projects.bafeditor.ui.components.ShadowUtils;

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
    private JPanel tilePropTreeHolder;
    private JPanel tilePanelHolder;
    private View mainView;

    public MainFrame() {
        setContentPane(root);

        UserContext.getInstance().setColorScheme(new DefaultScheme());
        Controller controller = new Controller();
        mainView = new View(controller);

        iconBar.add(mainView.getIconBarPanel(), BorderLayout.CENTER);
        fileListHolder.add(mainView.getFileList(), BorderLayout.CENTER);

        ModernScrollPane scrollPane = new ModernScrollPane(mainView.getTileBrowser());
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);

        tileBrowserHolder.add(scrollPane, BorderLayout.CENTER);
        tileViewerHolder.add(mainView.getTileViewer(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        tilePropTreeHolder.add(mainView.getTilePropertiesTree(), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_BOTH, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        root.setBackground(UserContext.getInstance().getColorScheme().getBackground());

        setTitle(APP_NAME);
//        setIconImage(Toolkit.getDefaultToolkit().getImage(logoURL));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void createUIComponents() {
        root = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                ShadowUtils.drawLineShadow(g, 0, -2, getWidth() + 1, -2, true);
            }
        };
    }
}
