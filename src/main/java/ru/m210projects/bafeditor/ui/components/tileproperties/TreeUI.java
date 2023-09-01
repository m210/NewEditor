package ru.m210projects.bafeditor.ui.components.tileproperties;

import ru.m210projects.bafeditor.ui.models.ObjectTreeNode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

public class TreeUI extends BasicTreeUI {

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        tree.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Release editable panel for repaint it while resizing
                stopEditing(tree);
            }
        });

        tree.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                TreePath path = getClosestPathForLocation(tree, e.getX(), e.getY());

                if (path != null && !path.equals(tree.getSelectionPath())) {
                    // Allow to use listeners in node when mouse on it
                    tree.setSelectionPath(path);
                    startEditing(path, e);
                }
            }
        });
    }

    @Override
    public Rectangle getPathBounds(JTree tree, TreePath path) {
        // Ability for panels to match bounds to tree
        Rectangle bounds = this.treeState.getBounds(path, new Rectangle());
        if (bounds != null) {
            Rectangle treeBounds = tree.getBounds();
            bounds.x = treeBounds.x;
            bounds.y += tree.getInsets().top;
            bounds.width = treeBounds.width;
        }
        return bounds;
    }

    @Override
    public void paint(Graphics gr, JComponent c) {
        Rectangle paintBounds = tree.getVisibleRect();
        TreePath initialPath = getClosestPathForLocation(tree, 0, paintBounds.y);
        Enumeration<TreePath> paintingEnumerator = treeState.getVisiblePathsFrom(initialPath);

        if (paintingEnumerator == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) gr.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Insets treeInsets = tree.getBorder().getBorderInsets(tree);

        super.paint(gr, c);

        int endY = paintBounds.y + paintBounds.height;
        int lastY = paintBounds.y;

        paintingEnumerator = treeState.getVisiblePathsFrom(initialPath);
        while (paintingEnumerator.hasMoreElements()) {
            TreePath path = paintingEnumerator.nextElement();

            if (path != null) {
                Rectangle bounds = getPathBounds(tree, path);
                if (bounds == null) {
                    continue;
                }

                lastY = bounds.y + bounds.height;
                if (bounds.y >= endY) {
                    break;
                }

                // Draw lines between paths
                g2d.drawLine(treeInsets.left, bounds.y, paintBounds.width - treeInsets.left, bounds.y);
            }
        }

        // Draw bottom line
        g2d.drawLine(treeInsets.left, lastY, paintBounds.width - treeInsets.left, lastY);
        g2d.dispose();
    }

    @Override
    protected void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        if (editingComponent != null && editingRow == row) {
            return;
        }

        ObjectTreeNode node = (ObjectTreeNode) path.getLastPathComponent();
        if (node.getValue() instanceof TreeGroup) {
            ((TreeGroup) node.getValue()).setExpanded(isExpanded);
        }

        int leadIndex = tree.hasFocus() ? getLeadSelectionRow() : -1;
        Component component = currentCellRenderer.getTreeCellRendererComponent(tree, node, tree.isRowSelected(row), isExpanded, isLeaf, row, (leadIndex == row));
        bounds = getPathBounds(tree, path);
        rendererPane.paintComponent(g, component, tree, bounds.x, bounds.y, bounds.width, bounds.height, true);
    }

    @Override
    protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
    }

    @Override
    protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) {
    }
}
