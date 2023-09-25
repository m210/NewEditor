package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.ui.FontUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;

import static javax.swing.BoxLayout.X_AXIS;

public class MaterialComboBox extends JPanel {

    private final JComboBox<?> comboBox;
    private MaterialLabel materialLabel;
    private boolean isPopupShown = false;

    public MaterialComboBox(JComboBox<?> comboBox) {
        this.comboBox = comboBox;

        this.comboBox.setOpaque(false);
        this.comboBox.setUI(new MaterialComboBoxUI());
        this.comboBox.setFont(FontUtilities.getDefaultFont().deriveFont(15.0f));
        this.comboBox.setBorder(new EmptyBorder(0, 10, 0, 0));
        String name = this.comboBox.getName();
        if (name != null) {
            addLabel(name);
        }

        this.setBorder(new EmptyBorder(13, 10, 13, 10));
        setLayout(new BoxLayout(this, X_AXIS));
        setOpaque(false);

        add(comboBox);
    }

    public MaterialComboBox addLabel(String labelText) {
        this.materialLabel = new MaterialLabel(this, labelText);
        materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getTootTipText());

        this.comboBox.addActionListener(e -> {
            if (this.comboBox.getHeight() != 0) {
                if (comboBoxIsEmpty() && materialLabel.offsetY != 0) {
                    materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getTootTipText());
                    materialLabel.moveTo(0);
                } else {
                    materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getButtonPressedBackground());
                    if (materialLabel.offsetY == 0) {
                        materialLabel.moveTo((-getHeight() / 2) + materialLabel.dimension.height / 2);
                    }
                }
            }
        });

        this.comboBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (comboBoxIsEmpty()) {
                    materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getTootTipText());
                } else {
                    materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getButtonPressedBackground());
                }

                if (comboBoxIsEmpty() && materialLabel.offsetY != 0) {
                    materialLabel.moveTo(0);
                } else {
                    repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                materialLabel.setFontColor(UserContext.getInstance().getColorScheme().getTootTipText());
                if (comboBoxIsEmpty() && materialLabel.offsetY != 0) {
                    materialLabel.moveTo(0);
                } else {
                    repaint();
                }
            }
        });
        return this;
    }

    private boolean comboBoxIsEmpty() {
        return comboBox.getSelectedIndex() == -1;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (materialLabel != null && materialLabel.offsetY == 0 && !comboBoxIsEmpty()) {
            materialLabel.offsetY = (-getHeight() / 2) + materialLabel.dimension.height / 2;
            materialLabel.setFontSize(13.0f);
        }

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        drawContainer(graphics);

        if (materialLabel != null) {
            Insets insets = this.getBorder().getBorderInsets(this);
            Rectangle rectangle = comboBox.getBounds();
            materialLabel.setCoordinates(rectangle.x + insets.left, getHeight() / 2);
            materialLabel.draw(graphics);
        }

        graphics.dispose();
    }

    protected void drawContainer(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f));
        if (!comboBox.hasFocus()) {
            graphics.setColor(UserContext.getInstance().getColorScheme().getSeparator());
        } else {
            graphics.setColor(UserContext.getInstance().getColorScheme().getButtonPressedBackground());
        }

        int x = 9;
        int y = 9;
        int width = getWidth() - 18;
        int height = getHeight() - 17;
        float radius = 16;

        Area area = new Area(new RoundRectangle2D.Float(x, y, width, height, radius, radius));
        if (isPopupShown) {
            area.add(new Area(new Rectangle2D.Float(x, y + height - radius / 2.0f, width, radius / 2.0f)));
        }
        graphics.draw(area);
    }

    protected void onShowPopup() {
        isPopupShown = true;
        repaint();
    }

    protected void onHidePopup() {
        isPopupShown = false;
        repaint();
    }

    public class MaterialComboBoxUI extends BasicComboBoxUI {

        private static final int LIST_ROW_HEIGHT = 40;
        private Color disabledTextColor;
        private Color enabledTextColor;

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            comboBox.setMaximumRowCount(8);

            applyColorScheme();
        }

        public void applyColorScheme() {
            this.enabledTextColor = UserContext.getInstance().getColorScheme().getText();
            this.disabledTextColor = UserContext.getInstance().getColorScheme().getSeparator();

            this.listBox.setBackground(UserContext.getInstance().getColorScheme().getBackground());
            this.arrowButton.setBackground(UserContext.getInstance().getColorScheme().getBackground());
            this.listBox.setForeground(enabledTextColor);
            this.arrowButton.setForeground(enabledTextColor);
            this.listBox.setSelectionBackground(UserContext.getInstance().getColorScheme().getOrderRollover());
            this.listBox.setSelectionForeground(enabledTextColor);

            // #SAM 18.08.2023 Заменил цвет выпадающего меню, т.к. обычно комбобокс в фокусе и у него другой цвет
            ((JComponent) this.popup).setBorder(new LineBorder(UserContext.getInstance().getColorScheme().getButtonPressedBackground(), 1));
        }

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton();
            button.setUI(new BasicButtonUI());
            button.setMinimumSize(new Dimension(35, 26));
            button.setBorder(null);
            button.setOpaque(false);
            button.setIcon(new EMEComboBoxIcon());
            return button;
        }

        @Override
        protected LayoutManager createLayoutManager() {
            return new ComboBoxLayoutManager() {

                @Override
                public void layoutContainer(Container parent) {
                    Insets insets = comboBox.getInsets();
                    int buttonWidth = arrowButton.getMinimumSize().width;
                    int buttonHeight = arrowButton.getMinimumSize().height;
                    arrowButton.setBounds(comboBox.getWidth() - insets.right - buttonWidth, comboBox.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
                }
            };
        }

        @Override
        protected ComboPopup createPopup() {
            return new MaterialPopupMenu(MaterialComboBox.this);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            listBox.setFont(FontUtilities.getDefaultFont().deriveFont(Font.PLAIN, 14.0f));
            if (comboBox.isEnabled()) {
                listBox.setForeground(enabledTextColor);
            } else {
                listBox.setForeground(disabledTextColor);
            }

            Component c = comboBox.getRenderer().getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);

            int x = bounds.x;
            int y = bounds.y;
            int w = bounds.width;
            int h = bounds.height;

            this.currentValuePane.paintComponent(g, c, this.comboBox, x, y, w, h, false);
        }

        public class EMEComboBoxIcon implements Icon, Serializable {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                int iconWidth = this.getIconWidth();
                int iconHeight = this.getIconHeight();
                g.translate(x, y);
                if (c.isEnabled()) {
                    if (comboBox.hasFocus()) {
                        g.setColor(UserContext.getInstance().getColorScheme().getButtonPressedBackground());
                    } else {
                        g.setColor(UserContext.getInstance().getColorScheme().getText());
                    }
                } else {
                    g.setColor(UserContext.getInstance().getColorScheme().getSeparator());
                }

                if (!isPopupShown) {
                    g.fillPolygon(new int[]{0, iconWidth / 2, iconWidth}, new int[]{0, iconHeight, 0}, 3);
                } else {
                    g.fillPolygon(new int[]{-1, iconWidth / 2, iconWidth}, new int[]{iconHeight + 1, 0, iconHeight + 1}, 3);
                }
                g.translate(-x, -y);
            }

            public int getIconWidth() {
                return 10;
            }

            public int getIconHeight() {
                return 5;
            }
        }

        public class MaterialPopupMenu extends BasicComboPopup {

            private final MaterialComboBox parent;

            public MaterialPopupMenu(MaterialComboBox combo) {
                super(combo.comboBox);
                this.parent = combo;
                this.setLightWeightPopupEnabled(false);
            }

            @Override
            public void setVisible(boolean visible) {
                if (visible == isVisible()) return;

                super.setVisible(visible);
                if (visible) {
                    parent.onShowPopup();
                } else {
                    parent.onHidePopup();
                }
            }

            @Override
            public void show(Component invoker, int x, int y) {
                super.show(invoker, x - 2, y);
            }

            @Override
            protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
                return super.computePopupBounds(px, parent.getBounds().height - 27, pw + 3, ph);
            }

            @Override
            protected int getPopupHeightForRowCount(int maxRowCount) {
                int minRowCount = Math.min(maxRowCount, this.comboBox.getItemCount());
                int height = 0;
                ListCellRenderer<Object> renderer = this.list.getCellRenderer();
                Object value;

                for (int i = 0; i < minRowCount; ++i) {
                    value = this.list.getModel().getElementAt(i);
                    renderer.getListCellRendererComponent(this.list, value, i, false, false);
                    height += LIST_ROW_HEIGHT;
                }

                if (height == 0) {
                    height = this.comboBox.getHeight();
                }

                Border border = this.scroller.getViewportBorder();
                Insets insets;
                if (border != null) {
                    insets = border.getBorderInsets(null);
                    height += insets.top + insets.bottom;
                }

                border = this.scroller.getBorder();
                if (border != null) {
                    insets = border.getBorderInsets(null);
                    height += insets.top + insets.bottom;
                }

                return height;
            }

            @Override
            protected JScrollPane createScroller() {
                JScrollPane sp = new ModernScrollPane(this.list);
                sp.setHorizontalScrollBar(null);
                return sp;
            }

            @Override
            protected void configureList() {
                super.configureList();
                list.setFixedCellHeight(LIST_ROW_HEIGHT);
                list.addListSelectionListener(e -> scroller.repaint());
            }
        }
    }
}
