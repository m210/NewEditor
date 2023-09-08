package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.ui.DefaultMouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class AngelChangerPanel extends JPanel implements DefaultMouseListener {

    private static final int BUTTON_RADIUS = 20;
    private final PointButton[] buttons;
    private final Color focusedBorderColor = new Color(0, 0, 0);
    private final Color idleBorderColor = new Color(214, 214, 214);
    private final Color rolloverButtonColor = new Color(128, 128, 128);
    private PointButton lastSelected = null;
    private final ChangeListener changeListener;

    public AngelChangerPanel(ChangeListener changeListener) {
        setOpaque(false);
        setBorder(new EmptyBorder(60, 80, 60, 80));
        addMouseListener(this);
        addMouseMotionListener(this);
        int pointCount = 8;
        this.buttons = new PointButton[pointCount];
        this.changeListener = changeListener;

        int[] degrees = new int[] { 225, 135, 45, 315, 180, 0, 270, 90 };
        for (int i = 0; i < pointCount; i++) {
            int deg = degrees[i];
            buttons[i] = new PointButton(deg, 0, 0, BUTTON_RADIUS);
        }
    }

    private void rebuildRectanglePoints(double width, double height, int maxPoints) {
        if (width <= 0 || height <= 0 || maxPoints < 4 || maxPoints > 32 || maxPoints % 2 != 0) {
            throw new IllegalArgumentException("Invalid width, height, or number of points");
        }

        buttons[0].setLocation(0, height);
        buttons[1].setLocation(width, height);
        buttons[2].setLocation(width, 0);
        buttons[3].setLocation(0, 0);

        if (maxPoints > 4) {
            int remainingPoints = maxPoints - 4;
            double horizontalSpacing = width / (remainingPoints / 2.0f + 1);
            double verticalSpacing = height / (remainingPoints / 2.0f + 1);

            for (int i = 1; i <= remainingPoints / 2; i++) {
                buttons[4].setLocation(i * horizontalSpacing, height);
                buttons[5].setLocation(i * horizontalSpacing, 0);
                buttons[6].setLocation(0, i * verticalSpacing);
                buttons[7].setLocation(width, i * verticalSpacing);
            }
        }

        int pointsWidth = (int) width;
        int pointsHeight = (int) height;
        int x0 = ((getWidth() - pointsWidth) - BUTTON_RADIUS) / 2;
        int y0 = ((getHeight() - pointsHeight) - BUTTON_RADIUS) / 2;

        for (PointButton p : buttons) {
            int x = (int) (x0 + p.getX());
            int y = (int) (y0 + p.getY());
            p.setLocation(x, y);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        rebuildRectanglePoints(getWidth() - 80, getHeight() - 60, 6);

        for (PointButton p : buttons) {
            if (p.isSelected()) {
                graphics.setColor(focusedBorderColor);
            } else if (p.isRollover()) {
                graphics.setColor(rolloverButtonColor);
            } else {
                graphics.setColor(idleBorderColor);
            }
            graphics.fillOval((int) p.getX(), (int) p.getY(), p.getRadius(), p.getRadius());
        }
    }

    protected void drawContainer(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setColor(getContainerBorderColor(hasFocus()));
        graphics.drawRoundRect(9, 9, getWidth() - 18, getHeight() - 17, 16, 16);
    }

    protected Color getContainerBorderColor(boolean focused) {
        if (!focused) {
            return idleBorderColor;
        }
        return focusedBorderColor;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (PointButton button : buttons) {
            if (button.intersects(e)) {
                button.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (PointButton button : buttons) {
            if (button.intersects(e)) {
                button.mouseEntered(e);
            } else if (button.isRollover()) {
                button.mouseExited(e);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    private class PointButton extends Point2D.Double implements DefaultMouseListener {
        private final int degrees;
        private final int radius;
        private boolean selected, rollover;

        public PointButton(int degrees, double x, double y, int radius) {
            super(x, y);
            this.degrees = degrees;
            this.radius = radius;
            if (degrees == 0) {
                selected = true;
                lastSelected = this;
            }
        }

        public int getRadius() {
            return radius;
        }

        public boolean isRollover() {
            return rollover;
        }

        public boolean isSelected() {
            return selected;
        }

        boolean intersects(MouseEvent e) {
            int mx = e.getX() - BUTTON_RADIUS / 2;
            int my = e.getY() - BUTTON_RADIUS / 2;
            return distance(mx, my) < radius / 2.0;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (lastSelected != null) {
                lastSelected.selected = false;
            }
            lastSelected = this;
            selected = true;
            changeListener.stateChanged(new ChangeEvent(degrees));
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            this.rollover = true;
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            this.rollover = false;
            repaint();
        }
    }
}
