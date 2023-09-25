package ru.m210projects.bafeditor.ui;

import java.awt.*;

public interface ColorScheme {
    String getName();

    Color getText();

    Color getInvertedText();

    Color getTootTipText();

    Color getIcon();

    Color getIconSelected();

    Color getIconRollover();

    Color getOrderSelection();

    Color getOrderRollover();

    Color getLink();

    Color getScrollbarTrack();

    Color getScrollbar();

    Color getScrollbarSelected();

    Color getButtonIdleBackground();

    Color getButtonInvertedText();

    Color getButtonFocusedBackground();

    Color getButtonPressedBackground();

    Color getButtonDisabledBackground();

    Color getBackground();

    Color getSeparator();


}
