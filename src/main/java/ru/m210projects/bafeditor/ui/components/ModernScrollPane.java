package ru.m210projects.bafeditor.ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * This is an implementation of a JScrollPane with a modern UI
 *
 * @author Philipp Danner
 */
public class ModernScrollPane extends JScrollPane {

    public static final int THUMB_SIZE = 6;
    public static final int SB_SIZE = THUMB_SIZE + 2;

    public ModernScrollPane(Component view) {
        this();
        viewport.setView(view);
    }

    public ModernScrollPane() {
        setBorder(null);

        // Set ScrollBar UI
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new ModernScrollBarUI(this));

        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        horizontalScrollBar.setOpaque(false);
        horizontalScrollBar.setUI(new ModernScrollBarUI(this));

        setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                boolean vsbNeeded = isVerticalScrollBarfNecessary();
                boolean hsbNeeded = isHorizontalScrollBarNecessary();

                if (vsb != null) {
                    vsb.setPreferredSize(new Dimension(SB_SIZE, -1));
                }

                if (hsb != null) {
                    hsb.setPreferredSize(new Dimension(-1, SB_SIZE));
                }

                super.layoutContainer(parent);

                if (colHead != null && colHead.isVisible() && vsbNeeded) {
                    Rectangle colHeadR = colHead.getBounds();
                    colHeadR.width += SB_SIZE;
                    colHead.setBounds(colHeadR);
                }

                if (viewport != null) {
                    Rectangle availR = viewport.getBounds();
                    if (vsbNeeded) {
                        availR.width += SB_SIZE;
                    }
                    if (hsbNeeded) {
                        availR.height += SB_SIZE;
                    }
                    viewport.setBounds(availR);
                }
            }
        });

        // Layering
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getHorizontalScrollBar(), 1);
        setComponentZOrder(getViewport(), 2);
    }

    private boolean isVerticalScrollBarfNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getHeight() > viewRect.getHeight();
    }

    private boolean isHorizontalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getWidth() > viewRect.getWidth();
    }

    /**
     * Class extending the BasicScrollBarUI and overrides all necessary methods
     */
    public static class ModernScrollBarUI extends BasicScrollBarUI {

        private JComponent sp;

        public ModernScrollBarUI(JComponent sp) {
            this.sp = sp;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new InvisibleScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setColor(Color.BLACK);
            Rectangle trackBounds = getTrackBounds();
            int orientation = scrollbar.getOrientation();

            int trackWidth = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : trackBounds.width;
            trackWidth = Math.max(trackWidth, THUMB_SIZE);

            int trackHeight = orientation == JScrollBar.VERTICAL ? trackBounds.height : THUMB_SIZE;
            trackHeight = Math.max(trackHeight, THUMB_SIZE);

            graphics2D.fillRect(trackBounds.x, trackBounds.y, trackWidth, trackHeight);

            int x = thumbBounds.x;
            int y = thumbBounds.y;

            int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
            width = Math.max(width, THUMB_SIZE);

            int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
            height = Math.max(height, THUMB_SIZE);

            Color color = Color.BLACK;
            if(isThumbRollover()) {
                color = Color.BLACK;
            }
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(color);
            graphics2D.fillRoundRect(x, y, width, height, 10, 10);
            graphics2D.dispose();
        }

        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
            super.setThumbBounds(x, y, width, height);
            sp.repaint();
        }

        /**
         * Invisible Buttons, to hide scroll bar buttons
         */
        private static class InvisibleScrollBarButton extends JButton {

            private static final long serialVersionUID = 1552427919226628689L;

            private InvisibleScrollBarButton() {
                setOpaque(false);
                setFocusable(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
}