package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.ui.FontUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class MaterialLabel {

    private final JComponent parent;
    /**
     * Относительная координата перемещения hint-надписи
     */
    int offsetY;
    Dimension dimension;
    /**
     * Координаты отрисовки hint-надписи
     */
    private int originX, originY;
    /**
     * Шрифт hint-надписи
     */
    private Font font;
    /**
     * Текущий цвет текста (их два)
     */
    private Color fontColor;
    /**
     * Текст hint-надписи
     */
    private String text;
    /**
     * Отступы от текста для отрисовки фона под текстом
     */
    private int horizontalInsets = 5;

    public MaterialLabel(JComponent parent, String text) {
        this.text = text;
        this.font = FontUtilities.getDefaultFont().deriveFont(15.0f);
        this.parent = parent;
        updateSize();
    }

    public void setCoordinates(int x, int y) {
        this.originX = x;
        this.originY = y;
    }

    public void setFont(Font font) {
        this.font = font;
        updateSize();
    }

    public void setText(String label) {
        this.text = label;
        updateSize();
    }

    public void updateSize() {
        FontMetrics fm = parent.getFontMetrics(font);
        this.dimension = new Dimension(fm.stringWidth(text), fm.getHeight());
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public void draw(Graphics2D graphics) {
        FontMetrics fm = parent.getFontMetrics(font);
        FontRenderContext frc = fm.getFontRenderContext();

        String value = text;
        int maxWidth = parent.getWidth();
        if (dimension.width > maxWidth) {
            value = FontUtilities.clipString(fm, text, maxWidth);
        }

        int textX = originX;
        int textY = offsetY + originY + dimension.height / 2 - fm.getDescent();

        graphics.setColor(parent.getBackground());
        graphics.fillRect(originX - horizontalInsets, textY - dimension.height + fm.getDescent(), dimension.width + 2 * horizontalInsets, dimension.height);

        graphics.setColor(fontColor);
        GlyphVector gv = font.createGlyphVector(frc, value);

        graphics.drawGlyphVector(gv, textX, textY);
    }

    public void setFontSize(float size) {
        font = font.deriveFont(Math.max(13.0f, Math.min(15.0f, size)));
        updateSize();
    }

    public void moveTo(int target) {
        final int start = offsetY;
        final int delta = target - start;
        final int msBetweenIterations = 5;

        Timer timer = new Timer(msBetweenIterations, new ActionListener() {
            final long animationTime = 150;
            final long nsBetweenIterations = msBetweenIterations * 1000000;
            final long startTime = System.nanoTime() - nsBetweenIterations;
            final long targetCompletionTime = startTime + animationTime * 1000000;
            final long targetElapsedTime = targetCompletionTime - startTime;

            @Override
            public void actionPerformed(ActionEvent e) {
                long timeSinceStart = System.nanoTime() - startTime;
                double percentComplete = Math.min(1.0, (double) timeSinceStart / targetElapsedTime);
                double factor = Math.pow(percentComplete, 3);
                offsetY = (int) Math.round(start + delta * factor);
                setFontSize((float) (font.getSize2D() + (delta < 0 ? -percentComplete : percentComplete) / 2.5f));

                parent.repaint();
                if (timeSinceStart >= targetElapsedTime) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.setInitialDelay(0);
        timer.start();
    }
}
