package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.ui.FontUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import static javax.swing.BoxLayout.X_AXIS;

public class MaterialTextField extends JPanel {

    private final JTextField editText;
    private Label label;

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
        this.label = new Label(labelText);
        label.setFontColor(getTootTipText());
        this.editText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                label.setFontColor(getContainerBorderColor(true));
                if (editText.getText().isEmpty()) {
                    label.moveTo((-getHeight() / 2) + label.dimension.height / 2);
                } else {
                    repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                label.setFontColor(getTootTipText());
                if (editText.getText().isEmpty()) {
                    label.moveTo(0);
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

        if (label != null && label.offsetY == 0 && !editText.getText().isEmpty()) {
            label.offsetY = (-getHeight() / 2) + label.dimension.height / 2;
            label.setFontSize(13.0f);
        }

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        if (label != null) {
            Insets insets = this.getBorder().getBorderInsets(this);
            Rectangle rectangle = editText.getBounds();
            label.setCoordinates(rectangle.x + insets.left, getHeight() / 2);
            label.draw(graphics);
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

    private Color getTootTipText() {
        return Color.GRAY;
    }

    protected void drawContainer(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setColor(getContainerBorderColor(editText.hasFocus()));
        graphics.drawRoundRect(9, 9, getWidth() - 18, getHeight() - 17, 16, 16);
    }

    class Label {

        /**
         * Относительная координата перемещения hint-надписи
         */
        int offsetY;

        /**
         * Координаты отрисовки hint-надписи
         */
        int originX, originY;

        Dimension dimension;

        /**
         * Шрифт hint-надписи
         */
        Font font;

        /**
         * Текущий цвет текста (их два)
         */
        Color fontColor;

        /**
         * Текст hint-надписи
         */
        String text;

        /**
         * Отступы от текста для отрисовки фона под текстом
         */
        int horizontalInsets = 5;

        public void setCoordinates(int x, int y) {
            this.originX = x;
            this.originY = y;
        }

        public Label(String text) {
            this.text = text;
            this.font = FontUtilities.getDefaultFont().deriveFont(15.0f);
            updateSize();
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
            FontMetrics fm = getFontMetrics(font);
            this.dimension = new Dimension(fm.stringWidth(text), fm.getHeight());
        }

        public void setFontColor(Color fontColor) {
            this.fontColor = fontColor;
        }

        public void draw(Graphics2D graphics) {
            FontMetrics fm = getFontMetrics(font);
            FontRenderContext frc = fm.getFontRenderContext();

            String value = text;
            int maxWidth = getWidth();
            if (dimension.width > maxWidth) {
                value = FontUtilities.clipString(fm, text, maxWidth);
            }

            int textX = originX;
            int textY = offsetY + originY + dimension.height / 2 - fm.getDescent();

            graphics.setColor(getBackground());
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

                    MaterialTextField.this.repaint();
                    if (timeSinceStart >= targetElapsedTime) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });

            timer.setInitialDelay(0);
            timer.start();
        }
    }
}
