package ru.m210projects.bafeditor.ui;

import javax.swing.*;
import java.awt.*;

public class FontUtilities {

    public static Font getDefaultFont() {
        return new JLabel().getFont(); // Constants.ROBOTO_FONT;
    }

    public static String clipString(FontMetrics fm, String string, int availTextWidth) {
        String clipString = "…";
        availTextWidth -= fm.stringWidth(clipString);
        if (availTextWidth <= 0) {
            return clipString;
        }

        int stringLength = string.length();
        int width = 0;
        for (int nChar = 0; nChar < stringLength; nChar++) {
            width += fm.charWidth(string.charAt(nChar));
            if (width > availTextWidth) {
                string = string.substring(0, nChar) + clipString;
                break;
            }
        }

        return string;
    }

    public static Dimension getTextDimension(Component component, String text) {
        if (text.isEmpty()) {
            return new Dimension(0, 0);
        }

        FontMetrics fm = component.getFontMetrics(component.getFont());
        int fontWidth = fm.stringWidth(text);
        int fontHeight = (fm.getHeight() - fm.getDescent() - 2);
        return new Dimension(fontWidth, fontHeight);
    }

    public static Dimension getTextDimension(Graphics graphics, String text) {
        if (text.isEmpty()) {
            return new Dimension(0, 0);
        }

        FontMetrics fm = graphics.getFontMetrics();
        int fontWidth = fm.stringWidth(text);
        int fontHeight = (fm.getHeight() - fm.getDescent() - 2);
        return new Dimension(fontWidth, fontHeight);
    }
}
