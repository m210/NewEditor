package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.ui.FontUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.BoxLayout.X_AXIS;

public class MaterialTextField extends JPanel {

    private final JTextField editText;
    private MaterialLabel materialLabel;

    public MaterialTextField(JTextField editText) {
        this.editText = editText;
        this.editText.setOpaque(false);
        this.editText.setFont(FontUtilities.getDefaultFont().deriveFont(15.0f));
        this.editText.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.setBorder(new EmptyBorder(20, 10, 20, 10));
        setLayout(new BoxLayout(this, X_AXIS));
        setOpaque(false);

        add(editText);
    }

    public MaterialTextField addLabel(String labelText) {
        this.materialLabel = new MaterialLabel(this, labelText);
        materialLabel.setFontColor(getTootTipTextColor());
        this.editText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                materialLabel.setFontColor(getContainerBorderColor(true));
                if (editText.getText().isEmpty()) {
                    materialLabel.moveTo((-getHeight() / 2) + materialLabel.dimension.height / 2);
                } else {
                    repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                materialLabel.setFontColor(getTootTipTextColor());
                if (editText.getText().isEmpty()) {
                    materialLabel.moveTo(0);
                } else {
                    repaint();
                }
            }
        });
        return this;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (materialLabel.offsetY == 0 && !editText.getText().isEmpty()) {
            materialLabel.offsetY = (-getHeight() / 2) + materialLabel.dimension.height / 2;
            materialLabel.setFontSize(13.0f);
        }

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        if (materialLabel != null) {
            Insets insets = this.getBorder().getBorderInsets(this);
            Rectangle rectangle = editText.getBounds();
            materialLabel.setCoordinates(rectangle.x + insets.left, getHeight() / 2);
            materialLabel.draw(graphics);
        }

        graphics.dispose();
    }

    protected Color getContainerBorderColor(boolean focused) {
        if (!focused) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }

    private Color getTootTipTextColor() {
        return Color.GRAY;
    }

    protected void drawContainer(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setColor(getContainerBorderColor(editText.hasFocus()));
        graphics.drawRoundRect(9, 9, getWidth() - 18, getHeight() - 17, 16, 16);
    }
}
