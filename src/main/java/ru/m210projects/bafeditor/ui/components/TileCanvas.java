package ru.m210projects.bafeditor.ui.components;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public class TileCanvas extends JPanel {

    protected IndexColorModel palette;
    int gridX = 0;
    int gridY = 0;
    private final Color gridColor = new Color(238, 238, 238);
    private final Color borderColor = new Color(214, 214, 214);
    private int gridSize = 16;

    private BufferedImage texture;

    public TileCanvas() {
        super(false);
        setBackground(Color.WHITE);
        this.texture = createTexture(1.0f);
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }

    protected BufferedImage createTexture(float scale) {
        int gridSize = (int) (this.gridSize * scale);
        final int cell = 2 * gridSize;
        BufferedImage texture = new BufferedImage(cell, cell, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = texture.createGraphics();
        gr.setColor(getBackground());
        gr.fillRect(0, gridSize, gridSize, gridSize);
        gr.fillRect(gridSize, 0, gridSize, gridSize);
        gr.setColor(gridColor);
        gr.fillRect(0, 0, gridSize, gridSize);
        gr.fillRect(gridSize, gridSize, gridSize, gridSize);
        gr.dispose();
        return texture;
    }

    public void setScale(float scale) {
        this.texture = createTexture(scale);
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        drawBackground(g2d);
        drawBorder(g2d);
        g2d.dispose();
    }

    public void setPalette(IndexColorModel palette) {
        this.palette = palette;
        repaint();
    }

    protected void drawBackground(Graphics2D g) {
        g.setPaint(new TexturePaint(texture, new Rectangle(gridX, gridY, texture.getWidth(), texture.getHeight())));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void drawBorder(Graphics g) {
        Rectangle view = this.getVisibleRect();
        g.setColor(borderColor);
        g.drawRect(0, 0, view.width - 1, view.height - 1);
    }
}
