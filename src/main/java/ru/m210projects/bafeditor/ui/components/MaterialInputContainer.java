package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.ui.FontUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.BoxLayout.X_AXIS;

public class MaterialInputContainer extends JPanel {

    private final JTextField editText;
    private int value;
    private final ChangeListener changeListener;

    private final Integer minValue, maxValue;


    public MaterialInputContainer(JComponent icon, ChangeListener changeListener, int minValue, int maxValue) {
        this.editText = new JFormattedTextField();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.editText.setOpaque(false);
        this.editText.setFont(FontUtilities.getDefaultFont().deriveFont(Font.PLAIN, 15.0f));
        this.editText.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.changeListener = changeListener;
        this.editText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                setValue(getValue());
                repaint();
            }
        });

        this.editText.addActionListener(e -> {
            int value = this.value;
            try {
                value = Integer.parseInt(editText.getText().replaceAll("[^\\d-]", ""));
            } catch (Exception ignored) {
            }
            setValue(value);
        });

        setValue(value);
        this.setLayout(new BoxLayout(this, X_AXIS));
        this.setBorder(new EmptyBorder(12, 20, 12, 10));
        this.setOpaque(false);

        MouseInputChanger mouseInputChanger = new MouseInputChanger(icon);
        icon.addMouseListener(mouseInputChanger);
        icon.addMouseMotionListener(mouseInputChanger);
        this.add(icon);
        this.add(editText);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        graphics.dispose();
    }

    protected void drawContainer(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        graphics.setColor(getContainerBorderColor(hasFocus() || editText.hasFocus()));
        graphics.drawRoundRect(9, 9, getWidth() - 18, getHeight() - 17, 16, 16);
    }

    protected Color getContainerBorderColor(boolean focused) {
        if (!focused) {
            return UserContext.getInstance().getColorScheme().getSeparator();
        }
        return UserContext.getInstance().getColorScheme().getButtonPressedBackground();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (minValue != null && value < minValue) {
            value = minValue;
        }

        if (maxValue != null && value > maxValue) {
            value = maxValue;
        }

        this.value = value;
        editText.setText(Integer.toString(value));
    }

    public class MouseInputChanger implements MouseListener, MouseMotionListener {

        private final JComponent parent;
        private Rectangle displayBounds;
        private Robot robot;
        private int value = 0;
        private int dx = 0;
        private int lastSuccessfulX = 0;

        public MouseInputChanger(JComponent parent) {
            this.parent = parent;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int mouseX = e.getXOnScreen();

            int oldValue = getValue();
            int newValue = value - (dx - mouseX) / 4;
            int direction = (int) Math.signum(lastSuccessfulX - mouseX);
            if ((direction == 1 && oldValue <= minValue || direction == -1 && oldValue >= maxValue)) {
                robot.mouseMove(lastSuccessfulX, e.getLocationOnScreen().y);
                return;
            }

            setValue(newValue);
            lastSuccessfulX = e.getXOnScreen();
            if (mouseX <= displayBounds.x) {
                dx += displayBounds.width;
                if (robot != null) {
                    robot.mouseMove(displayBounds.x + displayBounds.width - 1, e.getLocationOnScreen().y);
                }
            } else if (mouseX >= (displayBounds.x + displayBounds.width)) {
                dx -= displayBounds.width;
                if (robot != null) {
                    robot.mouseMove(displayBounds.x + 1, e.getLocationOnScreen().y);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            for (GraphicsDevice device : gs) {
                GraphicsConfiguration[] configurations = device.getConfigurations();
                for (GraphicsConfiguration config : configurations) {
                    Rectangle bounds = config.getBounds();
                    if (bounds.contains(e.getPoint())) {
                        value = getValue();
                        dx = e.getXOnScreen();
                        lastSuccessfulX = dx;
                        displayBounds = new Rectangle(bounds);
                        int displayGaps = 50;
                        displayBounds.x += displayGaps;
                        displayBounds.width -= 2 * displayGaps;

                        try {
                            robot = new Robot(device);
                        } catch (AWTException ex) {
                            robot = null;
                        }
                        return;
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            int newValue = getValue();
            if (newValue != value) {
                changeListener.stateChanged(new ChangeEvent(getValue()));
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }
}
