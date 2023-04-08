package ru.m210projects.bafeditor.ui.components.tileproperties;

import ru.m210projects.bafeditor.ui.models.ObjectTreeNode;
import ru.m210projects.bafeditor.ui.models.TileContainer;

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

    public TilePropertiesTree() {
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
    }

    private DefaultTreeModel buildTreeModel(TileContainer container) {
        DefaultMutableTreeNode style = new ObjectTreeNode("Root", null);
        DefaultMutableTreeNode parentNode = new ObjectTreeNode("Properties", new TreeGroup("Properties"));
        parentNode.add(new ObjectTreeNode("PropertiesPanel", new PropertiesPanel()));
        style.add(parentNode);

        parentNode = new ObjectTreeNode("Animation", new TreeGroup("Animation"));
        parentNode.add(new ObjectTreeNode("AnimationPanel", new AnimationPanel()));
        style.add(parentNode);

        parentNode = new ObjectTreeNode("Blood extras", new TreeGroup("Blood extras"));
        parentNode.add(new ObjectTreeNode("BloodExtras", new BloodExtrasPanel()));
        style.add(parentNode);
        return new DefaultTreeModel(style);
    }

    public void update(TileContainer container) {
        setModel(buildTreeModel(container));
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
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
