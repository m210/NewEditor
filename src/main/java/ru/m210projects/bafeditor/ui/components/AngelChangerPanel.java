package ru.m210projects.bafeditor.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class AngelChangerPanel extends JPanel implements MouseListener {

    private final int deltaAngle;
    private Color focusedBorderColor = new Color(0, 0, 0);
    private Color idleBorderColor = new Color(214, 214, 214);

    public AngelChangerPanel(int deltaAngle, ChangeListener changeListener) {
        setOpaque(false);
        setBorder(new EmptyBorder(60, 60, 60, 60));
        addMouseListener(this);
        this.deltaAngle = deltaAngle;
    }

    public static List<Point2D.Double> getRectanglePoints(double width, double height, int maxPoints) {
        if (width <= 0 || height <= 0 || maxPoints < 4 || maxPoints > 32 || maxPoints % 2 != 0) {
            throw new IllegalArgumentException("Invalid width, height, or number of points");
        }

        List<Point2D.Double> rectanglePoints = new ArrayList<>();
        rectanglePoints.add(new Point2D.Double(0, height));  // Вершина 1
        rectanglePoints.add(new Point2D.Double(width, height));  // Вершина 2
        rectanglePoints.add(new Point2D.Double(width, 0));  // Вершина 3
        rectanglePoints.add(new Point2D.Double(0, 0));  // Вершина 4

        if (maxPoints > 4) {
            int remainingPoints = maxPoints - 4;
            double horizontalSpacing = width / (remainingPoints / 2.0f + 1);
            double verticalSpacing = height / (remainingPoints / 2.0f + 1);

            for (int i = 1; i <= remainingPoints / 2; i++) {
                rectanglePoints.add(new Point2D.Double(i * horizontalSpacing, height));  // Дополнительные точки посередине верхней стороны
                rectanglePoints.add(new Point2D.Double(i * horizontalSpacing, 0));  // Дополнительные точки посередине нижней стороны
                rectanglePoints.add(new Point2D.Double(0, i * verticalSpacing));  // Дополнительные точки посередине левой стороны
                rectanglePoints.add(new Point2D.Double(width, i * verticalSpacing));  // Дополнительные точки посередине правой стороны
            }
        }

        return rectanglePoints;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        graphics.setColor(Color.ORANGE);

        int x0 = 20;
        int y0 = 20;

        List<Point2D.Double> points = getRectanglePoints(getWidth() - 60, getHeight() - 60, 6);
        for (Point2D.Double p : points) {
            int x = (int) (x0 + p.getX());
            int y = (int) (y0 + p.getY());
            graphics.fillOval(x, y, 10, 10);
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
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        int buttonCount = 360 / deltaAngle;


    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
