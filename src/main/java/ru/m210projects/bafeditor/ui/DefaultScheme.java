package ru.m210projects.bafeditor.ui;

import java.awt.*;

public class DefaultScheme implements ColorScheme {

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public Color getText() {
        return new Color(0x333333);
    }

    @Override
    public Color getInvertedText() {
        return new Color(0xFFFFFF);
    }

    @Override
    public Color getIcon() {
        return getText();
    }

    @Override
    public Color getIconSelected() {
        return getInvertedText();
    }

    @Override
    public Color getIconRollover() {
        return getText();
    }

    @Override
    public Color getOrderSelection() {
        return getText();
    }

    @Override
    public Color getOrderRollover() {
        return new Color(0xF3F3F3);
    }

    @Override
    public Color getTootTipText() {
        return new Color(0xBFBFBF);
    }

    @Override
    public Color getLink() {
        return new Color(0x16B4EF);
    }

    @Override
    public Color getScrollbarTrack() {
        return new Color(0x33626477, true); //20%
    }

    @Override
    public Color getScrollbar() {
        return new Color(0x99626477);
    }

    @Override
    public Color getScrollbarSelected() {
        return new Color(0xCC626477, true); //80%
    }

    @Override
    public Color getButtonIdleBackground() {
        return new Color(0x333333);
    }

    @Override
    public Color getButtonInvertedText() {
        return new Color(0xFFFFFF);
    }

    @Override
    public Color getButtonFocusedBackground() {
        return new Color(0x777777);
    }

    @Override
    public Color getButtonPressedBackground() {
        return new Color(0x1E1818);
    }

    @Override
    public Color getButtonDisabledBackground() {
        return new Color(0xC9CAD7);
    }

    @Override
    public Color getBackground() {
        return new Color(0xFFFFFF);
    }

    @Override
    public Color getSeparator() {
        return new Color(0xC9CAD7);
    }

}
