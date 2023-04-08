package ru.m210projects.bafeditor.ui.components;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class ShadowUtils {

    private static final int TOP_OPACITY = 80 / 10;
    private static final int SHADOW_SIZE = 6;

    public static void drawRoundRectShadow(Graphics g, int x, int y, int width, int height, int radius, boolean isInnerShadow) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        for (int i = 0; i < SHADOW_SIZE; i++) {
            int index = i;
            if (isInnerShadow) {
                index *= -1;
            }
            g2d.setColor(new Color(0, 0, 0, (TOP_OPACITY * (SHADOW_SIZE - i))));
            g2d.drawRoundRect(x - index, y - index, width + ((index * 2) + 1), height + ((index * 2) + 1), radius, radius);
        }

        g2d.dispose();
    }

    public static void drawRectShadow(Graphics g, int x, int y, int width, int height, boolean isInnerShadow) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        for (int i = 0; i < SHADOW_SIZE; i++) {
            int index = i;
            if (isInnerShadow) {
                index *= -1;
            }
            g2d.setColor(new Color(0, 0, 0, (TOP_OPACITY * (SHADOW_SIZE - i))));
            g2d.drawRect(x - index, y - index, width + ((index * 2) + 1), height + ((index * 2) + 1));
        }
    }

    public static void drawAreaShadow(Graphics g, Area area, int dx, int dy, float basicScaleX, float basicScaleY, boolean isInnerShadow) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Rectangle r = area.getBounds();
        float scaleX, scaleY, translate;
        if (isInnerShadow) {
            scaleX = (r.width - 2.0f) / r.width;
            scaleY = (r.height - 2.0f) / r.height;
            translate = 1;
        } else {
            scaleX = (r.width + 2.0f) / r.width;
            scaleY = (r.height + 2.0f) / r.height;
            translate = -1;
        }

        g2d.transform(new AffineTransform(basicScaleX, 0, 0, basicScaleY, dx, dy));
        for (int i = 0; i < SHADOW_SIZE; i++) {
            g2d.setColor(new Color(0, 0, 0, (TOP_OPACITY * (SHADOW_SIZE - i))));
            g2d.transform(new AffineTransform(scaleX, 0, 0, scaleY, translate, translate));
            g2d.draw(area);
        }

        g2d.dispose();
    }

    public static void drawLineShadow(Graphics g, int x1, int y1, int x2, int y2, boolean isInnerShadow) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        for (int i = 0; i < SHADOW_SIZE; i++) {
            int index = i;
            if (isInnerShadow) {
                index *= -1;
            }
            g2d.setColor(new Color(0, 0, 0, (TOP_OPACITY * (SHADOW_SIZE - i))));
            g2d.drawLine(x1 - index, y1 - index, x2 - index, y2 - index);
        }
    }
}
