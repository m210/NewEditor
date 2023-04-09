package ru.m210projects.bafeditor.ui.components.tileproperties;

import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.backend.tiles.AnimType;
import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.ui.Controller;
import ru.m210projects.bafeditor.ui.models.ObjectTreeNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class TilePropertiesTree extends JTree {

    public static final int TOP_PADDING = 15;
    public static final int BOTTOM_PADDING = 15;
    public static final int GROUP_HEIGHT = 15;


    private final PropertiesPanel propertiesPanel;
    private final AnimationPanel animationPanel;
    private final BloodExtrasPanel bloodExtrasPanel;

    public TilePropertiesTree(Controller controller) {
        this.setUI(new TreeUI());
        this.setBorder(new EmptyBorder(5, 0, 5, 0));
        this.setCellRenderer(new TileTreeRenderer());
        this.setCellEditor(new TileTreeEditor());
        this.setEditable(true);

        setRootVisible(false);
        setVisibleRowCount(6);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setLargeModel(true); // для обновления всей модели при изменении выбранного пункта

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) getCellRenderer();

        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        renderer.setBackgroundSelectionColor(null);
        renderer.setBackgroundNonSelectionColor(null);
        renderer.setBorderSelectionColor(null);

        this.propertiesPanel = new PropertiesPanel();
        this.animationPanel = new AnimationPanel(controller);
        this.bloodExtrasPanel = new BloodExtrasPanel();
        setModel(initTreeModel(propertiesPanel, animationPanel, bloodExtrasPanel));
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    private DefaultTreeModel initTreeModel(PropertiesPanel propertiesPanel, AnimationPanel animationPanel, BloodExtrasPanel bloodExtrasPanel) {
        DefaultMutableTreeNode style = new ObjectTreeNode("Root", null);
        DefaultMutableTreeNode parentNode = new ObjectTreeNode("PropertiesGroup", new TreeGroup("Properties"));
        parentNode.add(new ObjectTreeNode("PropertiesPanel", propertiesPanel));
        style.add(parentNode);

        parentNode = new ObjectTreeNode("AnimationGroup", new TreeGroup("Animation"));
        parentNode.add(new ObjectTreeNode("AnimationPanel", animationPanel));
        style.add(parentNode);

        parentNode = new ObjectTreeNode("BloodExtrasGroup", new TreeGroup("Blood extras"));
        parentNode.add(new ObjectTreeNode("BloodExtrasPanel", bloodExtrasPanel));
        style.add(parentNode);
        return new DefaultTreeModel(style);
    }

    public void update(int tile) {
//        viewAngle = 0;
//        angleLabel.setText("Angle: " + viewAngle);

        ArtEntry pic = UserContext.getInstance().getArtEntry(tile);
        if (pic.exists()) {
            if (pic.getType() != AnimType.NONE) {
                animationPanel.getAnimationTrigger().setText("Stop");
            } else {
                animationPanel.getAnimationTrigger().setText("Start");
            }
        } else {
            animationPanel.getAnimationTrigger().setText("Start");
        }
        repaint();
    }

    public PropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public AnimationPanel getAnimationPanel() {
        return animationPanel;
    }

    public BloodExtrasPanel getBloodExtrasPanel() {
        return bloodExtrasPanel;
    }

    public static class TileTreeRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof ObjectTreeNode) {
                ObjectTreeNode node = (ObjectTreeNode) value;
                if (node.getValue() instanceof JPanel) {
                    return (JPanel) node.getValue();
                }
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }

        @Override
        public Color getBackground() {
            return null;
        }
    }

    private static class TileTreeEditor extends AbstractCellEditor implements TreeCellEditor {
        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                Point p = ((MouseEvent) e).getPoint();
                TreePath path = ((JTree) e.getSource()).getClosestPathForLocation(p.x, p.y);
                if (path != null) {
                    ObjectTreeNode node = (ObjectTreeNode) path.getLastPathComponent();
                    return node.getValue() instanceof JPanel && !(node.getValue() instanceof TreeGroup);
                }
            }
            return false;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
            if (value instanceof ObjectTreeNode) {
                ObjectTreeNode node = (ObjectTreeNode) value;
                if (node.getValue() instanceof JPanel) {
                    return (Component) node.getValue();
                }
            }
            return renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);
        }
    }
}
