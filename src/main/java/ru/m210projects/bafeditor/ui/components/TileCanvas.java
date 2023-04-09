package ru.m210projects.bafeditor.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;

public class TileCanvas extends JPanel {

    float crossX = 0;
    float crossY = 0;
    private Color gridColor = new Color(238, 238, 238);
    private Color borderColor = new Color(214, 214, 214);
    private int gridSize = 16;

    protected IndexColorModel palette;

    public TileCanvas() {
        setBackground(Color.WHITE);
    }

    @Override
    public void paint(Graphics g) {
        drawBackground(g);
        drawBorder(g);
    }

    public void setPalette(IndexColorModel palette) {
        this.palette = palette;
    }

    private void drawBackground(Graphics g) {
        final int cell = 2 * gridSize;
        Rectangle view = this.getVisibleRect();
        g.setColor(getBackground());
        g.fillRect(0, 0, view.width, view.height);
        g.setColor(gridColor);
        int startX = ((int) crossX) % cell - cell;
        int startY = ((int) crossY) % cell - cell;

        int col = 0;
        for (int y = startY; y < view.height; y += gridSize) {
            int x = startX;
            if(col % 2 == 0) {
                x -= gridSize;
            }
            for (; x < view.width; x += cell) {
                g.fillRect(x, y, gridSize, gridSize);
            }
            col++;
        }
    }

    protected void drawBorder(Graphics g) {
        Rectangle view = this.getVisibleRect();
        g.setColor(borderColor);
        g.drawRect(0, 0, view.width - 1, view.height - 1);
    }
}
