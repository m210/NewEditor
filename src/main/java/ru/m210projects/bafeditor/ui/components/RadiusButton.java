package ru.m210projects.bafeditor.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;

public class RadiusButton extends JButton {

    private int radius = 12;
    private Color idleBackground = Color.WHITE;
    private Color focusedBackground = Color.GRAY;
    private Color pressedBackground = Color.WHITE;
    private Color disabledBackground = Color.WHITE;
    private Color idleText = Color.BLACK;
    private Color focusedText = Color.BLACK;
    private Color pressedText = Color.BLACK;
    private Color disabledText = Color.BLACK;
    private Color borderColor = new Color(214, 214, 214);

    public RadiusButton(String text, ActionListener callback) {
        setText(text);
        setOpaque(false);
        setBorder(new EmptyBorder(6, 6, 6, 6));
        setFocusPainted(false);
        setUI(new BasicButtonUI());
        if (callback != null) {
            addActionListener(callback);
        }
        // setFont(Constants.ROBOTO_FONT.deriveFont(15.0f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(isEnabled()) {
            if (getModel().isPressed()) {
                setForeground(pressedText);
                g.setColor(pressedBackground);
                g.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, radius, radius);
            } else if (getModel().isRollover() /*|| hasFocus()*/) {
                setForeground(focusedText);
                g.setColor(focusedBackground);
                g.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, radius, radius);
            } else {
                setForeground(idleText);
                g.setColor(idleBackground);
                g.fillRoundRect(1, 1, getWidth() - 1, getHeight() - 1, radius, radius);
            }

            g.setColor(borderColor);
            g.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);
        } else {
            setForeground(disabledText);
            g.setColor(idleBackground);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g.setColor(disabledBackground);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        super.paintComponent(g);
    }
}
