package ru.m210projects.bafeditor.ui.components.tileproperties;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.ui.Controller;
import ru.m210projects.bafeditor.ui.components.MaterialInputContainer;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import static com.intellij.uiDesigner.core.GridConstraints.*;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;

public class PropertiesPanel extends JPanel {

    private final JTextField tileWidth;
    private final JTextField tileHeight;
    private final JTextField tileChecksum; // FIXME: не обновляется
    private final MaterialInputContainer xOffsetContainer;
    private final MaterialInputContainer yOffsetContainer;
    public PropertiesPanel(Controller controller) {
        super(false);
        setBorder(new EmptyBorder(TilePropertiesTree.TOP_PADDING, 0, TilePropertiesTree.BOTTOM_PADDING, 0));
        setLayout(new GridLayoutManager(4, 2));

        add(new JLabel("Tile width:"), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(tileWidth = new JTextField("N/A"), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new JLabel("Tile height:"), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(tileHeight = new JTextField("N/A"), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(new JLabel("Tile checksum:"), new GridConstraints(2, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(tileChecksum = new JTextField("N/A"), new GridConstraints(2, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        add(xOffsetContainer = new MaterialInputContainer(new JLabel("X"), controller::onXOffsetChanged, (byte) -128, (byte) 127), new GridConstraints(3, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));
        add(yOffsetContainer = new MaterialInputContainer(new JLabel("Y"), controller::onYOffsetChanged, (byte) -128, (byte) 127), new GridConstraints(3, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null));

        this.tileWidth.setEditable(false);
        this.tileHeight.setEditable(false);
        this.tileChecksum.setEditable(false);
        this.setOpaque(false);
    }

    public void onTileSelected(int tile) {
        UserContext context = UserContext.getInstance();

        ArtEntry pic = context.getArtEntry(tile);
        if (pic.exists()) {
            tileWidth.setText(Integer.toString(pic.getWidth()));
            tileHeight.setText(Integer.toString(pic.getHeight()));
            tileChecksum.setText(Long.toString(pic.getChecksum()));
            xOffsetContainer.setValue(pic.getOffsetX());
            yOffsetContainer.setValue(pic.getOffsetY());
        } else {
            tileWidth.setText("N/A");
            tileHeight.setText("N/A");
            tileChecksum.setText("N/A");
            xOffsetContainer.setValue(0);
            yOffsetContainer.setValue(0);
        }
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }
}
