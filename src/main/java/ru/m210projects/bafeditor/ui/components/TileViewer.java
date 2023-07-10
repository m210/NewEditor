package ru.m210projects.bafeditor.ui.components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.backend.tiles.AnimType;
import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.AnimationRunnable;
import ru.m210projects.bafeditor.ui.Controller;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

import static com.intellij.uiDesigner.core.GridConstraints.*;

public class TileViewer extends JPanel {

    private final TileCanvas viewer;
    private final int prevColor = new Color(0, 0, 255).getRGB();
    private final int nextColor = new Color(255, 0, 0).getRGB();
    public float scale = 1.0f;
    public boolean nextTile, prevTile;
    private int crossX, crossY;
    private boolean crossEnabled, repeatTexture, flipX;
    private int selectedTile = -1;
    private final AnimationRunnable anim;
    private final Thread anmTh;
    private volatile int animIndex = 0;

    public TileViewer(Controller controller) {
        super(false);
        this.viewer = new TileCanvas() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                drawViewer(g);
            }
        };

        MouseAdapter adapter = new MouseAdapter() {
            private int mouseX, mouseY;

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Rectangle bounds = getBounds();
                if (bounds != null && bounds.contains(x, y)) {
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                setCross(e.getX() + mouseX, e.getY() + mouseY);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    resetPosition();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = crossX - e.getX();
                mouseY = crossY - e.getY();
            }
        };

        viewer.addMouseListener(adapter);
        viewer.addMouseMotionListener(adapter);

        anim = new AnimationRunnable() {
            @Override
            public void process(int clock) {
                UserContext context = UserContext.getInstance();
                ArtEntry pic = context.getCurrentEntry();
                animIndex = 0;
                if (pic.getType() != AnimType.NONE) {
                    int index = 0;
                    int frames = pic.getFrames();

                    if (frames > 0) {
                        switch (pic.getType()) {
                            case OSCIL:
                                index = clock % (frames * 2);
                                if (index >= frames) {
                                    index = frames * 2 - index;
                                }
                                break;
                            case FORWARD:
                                index = clock % (frames + 1);
                                break;
                            case BACKWARD:
                                index = -(clock % (frames + 1));
                            default:
                                break;
                        }
                    }

                    animIndex = index;
                    repaint();
                } else {
                    pause();
                }
            }
        };
        anmTh = new Thread(anim);
        anmTh.setName("BAFed-Animation-Thread");
        anmTh.start();

        Dimension dimension = new Dimension(256, 256);
        setCross(dimension.width / 2, dimension.height / 2);

        setLayout(new GridLayoutManager(2, 1));
        add(viewer, new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, dimension, null, 0, false));

        JPanel propButtonsHolder = new JPanel();
        propButtonsHolder.setOpaque(false);
        propButtonsHolder.setLayout(new GridLayoutManager(2, 3));
        propButtonsHolder.add(new RadiusButton("Fill", controller::onFillButtonClicked), new GridConstraints(0, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Reset position", controller::onResetPositionButtonClicked), new GridConstraints(0, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Reset zoom", controller::onResetZoomButtonClicked), new GridConstraints(0, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Cross", controller::onCrossButtonClicked), new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Prev. contour", controller::onPrevContourButtonClicked), new GridConstraints(1, 1, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        propButtonsHolder.add(new RadiusButton("Next contour", controller::onNextContourButtonClicked), new GridConstraints(1, 2, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_FIXED, SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(propButtonsHolder, new GridConstraints(1, 0, 1, 1, ANCHOR_NORTH, FILL_HORIZONTAL, SIZEPOLICY_WANT_GROW | SIZEPOLICY_CAN_SHRINK, SIZEPOLICY_FIXED, null, null, null, 0, false));
        setOpaque(false);
    }

    @Override
    public void setUI(PanelUI ui) {
        /* nothing */
    }

    public Dimension getCanvasSize() {
        return viewer.getSize();
    }

    public void setPalette(IndexColorModel palette) {
        viewer.setPalette(palette);
    }

    public void setSelectedTile(int tile) {
        selectedTile = tile;
        animIndex = 0;
        repaint();
    }

    public void setCross(int crossX, int crossY) {
        this.crossX = crossX;
        this.crossY = crossY;
        viewer.gridX = crossX;
        viewer.gridY = crossY;
    }

    public void switchCross() {
        crossEnabled = !crossEnabled;
        repaint();
    }

    public void switchNextTile() {
        this.nextTile = !nextTile;
        repaint();
    }

    public void switchPrevTile() {
        this.prevTile = !prevTile;
        repaint();
    }

    public void resetPosition() {
        setCross(viewer.getWidth() / 2, viewer.getHeight() / 2);
        repaint();
    }

    public void setScale(float scale) {
        this.scale = scale;
        viewer.setScale(scale);
        repaint();
    }

    public void switchRepeatTexture() {
        repeatTexture = !repeatTexture;
        repaint();
    }

    public void timerUpdate() {
        anmTh.interrupt();
    }

    public boolean isAnimationRunning() {
        return anim.isRunning();
    }

    public void startAnimation() {
        UserContext context = UserContext.getInstance();

        ArtEntry pic = context.getCurrentEntry();
        if (pic.getType() == AnimType.NONE) {
            anim.pause();
            return;
        }

        anim.setTimer(10 << pic.getSpeed());
        if (!anim.isRunning()) {
            anim.resume();
        }
    }

    public void stopAnimation() {
        anim.pause();
    }

    private synchronized void drawViewer(Graphics g) {
        UserContext context = UserContext.getInstance();

        startAnimation();

        drawTile(g, context.getArtEntry(selectedTile + animIndex));

        if (prevTile) {
            drawContour(g, context.getArtEntry(selectedTile - 1), scale, prevColor);
        }

        if (nextTile) {
            drawContour(g, context.getArtEntry(selectedTile + 1), scale, nextColor);
        }

        if (!repeatTexture && crossEnabled) {
            drawCross(g);
        }
        flipX = false; // FIXME
    }

    private void drawCross(Graphics src) {
        Graphics2D g2ds = (Graphics2D) src;
        Stroke thindashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{8.0f, 5.0f, 2.0f, 5.0f}, 0.0f);
        src.setColor(new Color(255, 255, 255));
        src.drawLine(0, crossY, getWidth(), crossY);
        src.drawLine(crossX, 0, crossX, getHeight());
        src.setColor(new Color(255, 0, 0));
        g2ds.setStroke(thindashed);
        src.drawLine(0, crossY, getWidth(), crossY);
        src.drawLine(crossX, 0, crossX, getHeight());
    }

    private void drawTile(Graphics src, ArtEntry pic) {
        if (pic.getSize() == 0) {
            return;
        }

        BufferedImage img = scale(pic.getRaster(viewer.palette), scale);
        if (repeatTexture) {
            TexturePaint background = new TexturePaint(img, new Rectangle(crossX, crossY, img.getWidth(), img.getHeight()));
            Graphics2D g2d = (Graphics2D) src.create();
            g2d.setPaint(background);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        } else {
            int x = crossX - (int) ((pic.getOffsetX() + pic.getWidth() / 2) * (flipX ? -scale : scale));
            int y = crossY - (int) ((pic.getOffsetY() + pic.getHeight() / 2) * scale);

            src.drawImage(img, x, y, null);

            drawBorder(src, x, y, (int) (pic.getWidth() * scale), (int) (pic.getHeight() * scale), Color.BLACK);
        }
    }

    private void drawBorder(Graphics src, int x, int y, int w, int h, Color color) {
        Graphics2D g2d = (Graphics2D) src;

        Stroke thindashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[]{2.0f, 2.0f}, 0.0f);

        g2d.setColor(color);
        g2d.setStroke(thindashed);
        g2d.drawRect(x, y, w, h);
    }

    private BufferedImage scale(BufferedImage old, float scale) {
        float w = Math.max(old.getWidth(), 1.0f);
        float h = Math.max(old.getHeight(), 1.0f);

        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);

        if (flipX) {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-w, 0));
        }

        BufferedImage after = new BufferedImage((int) (w * scale), (int) (h * scale), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) after.getGraphics();
        g2d.setTransform(at);
        g2d.drawImage(old, 0, 0, null);

        return after;
    }

    private void drawContour(Graphics src, ArtEntry pic, float scale, int colorRGB) {
        if (pic.getSize() == 0) {
            return;
        }

        BufferedImage raster = pic.getRaster(viewer.palette);
        ColorModel palette = raster.getColorModel();
        BufferedImage buf = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int[] tmp = new int[1];
        for (int x = 0; x < pic.getWidth(); x++) {
            for (int y = 0; y < pic.getHeight(); y++) {
                raster.getData().getPixel(x, y, tmp);
                int index = tmp[0];

                if (index == 255) {
                    continue;
                }

                int color = colorRGB | palette.getRGB(index);
                int rc = (color >> 16) & 0xff;
                int gc = (color >> 8) & 0xff;
                int bc = color & 0xff;
                int alpha = 128;

                buf.setRGB(x, y, (alpha << 24) | (rc << 16) | (gc << 8) | bc);
            }
        }

        BufferedImage next_tile = scale(buf, scale);

        int x = crossX - (int) ((pic.getOffsetX() + pic.getWidth() / 2) * (flipX ? -scale : scale));
        int y = crossY - (int) ((pic.getOffsetY() + pic.getHeight() / 2) * scale);

        src.drawImage(next_tile, x, y, null);
    }
}
