package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.backend.tiles.ViewType;
import ru.m210projects.bafeditor.ui.models.BloodData;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TileBrowser extends TileCanvas {

    private static final int BACKGROUND_BORDER = 4;
    private final Color voxelBackground = new Color(0xFFD0D0);
    private final Color surfaceBackground = new Color(0xD0DDFF);
    private final Color viewTypeBackground = Color.GREEN;
    private final Color tileNumberBackground = Color.WHITE;

    private int cellWidth = 128;
    private int cellHeight = 128;
    private boolean fillThumbnails;
    private List<ArtEntry> list = new ArrayList<>();
    private BufferedImage[] tiles = new BufferedImage[0];
    private int firstTile = 0;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle view = this.getVisibleRect();
        ShadowUtils.drawRectShadow(g, 0, 0, view.width - 1, view.height - 1, true);

        BufferedImage bf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bf.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 9));

        int tilecols = getCols();
        int tilerows = (getHeight() + cellHeight) / cellHeight;

        for (int i = 0; i < tilecols * tilerows; i++) {
            ArtEntry pic = list.get(i);

            int x = (i % tilecols) * cellWidth;
            int y = (i / tilecols) * cellHeight;
            int tile = i + firstTile;

            g2d.drawImage(getTileImage(pic, cellWidth, cellHeight), x, y, null);

            x = 3 + x;
            y = y + cellHeight - 10 - BACKGROUND_BORDER;
            ViewType viewType = pic.getViewType();
            if (viewType != ViewType.SINGLE) {
                Rectangle textRect = drawText(g2d, viewType.toString(), x, y, Color.BLACK, viewTypeBackground);
                y -= (textRect.height + 1);
            }

            BloodData bloodData = UserContext.getInstance().getBloodData();
            if (bloodData != null) {
                String surface = bloodData.getSurfaceName(tile);
                if (!surface.isEmpty()) {
                    Rectangle textRect = drawText(g2d, surface, x, y, Color.BLACK, surfaceBackground);
                    y -= (textRect.height + 1);
                }

                String voxel = bloodData.getVoxelId(tile);
                if (!voxel.isEmpty()) {
                    Rectangle textRect = drawText(g2d, voxel, x, y, Color.BLACK, voxelBackground);
                    y -= (textRect.height + 1);
                }
            }

            drawText(g2d, Integer.toString(tile), x, y, Color.BLACK, tileNumberBackground);
        }

        g2d.dispose();
        g.drawImage(bf, 0, 0, null);
    }

    public void update(ArtFile artFile) {
        this.list = artFile.getEntries();
        this.tiles = new BufferedImage[list.size()];
        this.firstTile = artFile.getFirstTile();
        repaint();
    }

    private int getRows() {
        return Math.max(getHeight() / cellHeight, 1);
    }

    private int getCols() {
        return Math.max(getWidth() / cellWidth, 1);
    }

    private Rectangle drawText(Graphics2D g2d, String text, int x, int y, Color textColor, Color backgroundColor) {
        Rectangle bounds = getStringBounds(g2d, text);
        if (backgroundColor != null) {
            g2d.setPaint(backgroundColor);
            g2d.fillRoundRect(x, y, bounds.width + BACKGROUND_BORDER, bounds.height + BACKGROUND_BORDER, BACKGROUND_BORDER, BACKGROUND_BORDER);
        }
        g2d.setPaint(textColor);
        final int borderOffset = BACKGROUND_BORDER / 2;
        g2d.drawString(text, x + borderOffset, y + bounds.height + borderOffset);

        bounds.width += BACKGROUND_BORDER;
        bounds.height += BACKGROUND_BORDER;
        return bounds;
    }

    private Rectangle getStringBounds(Graphics2D g2, String str) {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, 0, 0);
    }

    private BufferedImage getTileImage(ArtEntry pic, int cellWidth, int cellHeight) {
        BufferedImage image = tiles[pic.getNum() - firstTile];
        if (image == null) {
            image = new BufferedImage(Math.max(1, pic.getWidth()), Math.max(1, pic.getHeight()), BufferedImage.TYPE_BYTE_INDEXED, palette);

            int[] tmp = new int[1];
            try(InputStream is = pic.getInputStream()) {
                for (int k = 0; k < pic.getSize(); k++) {
                    int row = (int) Math.floor(k / (double) pic.getHeight());
                    int col = k % pic.getHeight();
                    tmp[0] = is.read();
                    image.getRaster().setPixel(row, col, tmp);
                }
            } catch (IOException e) {
                System.err.println("Failed to load tile " + pic.getNum());
                System.err.println(e.getMessage());
            }
        }

        double w = image.getWidth();
        double h = image.getHeight();
        BufferedImage img = new BufferedImage(cellWidth, cellHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        double kt = w / h;
        double kv = cellWidth / (double) cellHeight;
        double scale;
        if (kv >= kt) {
            scale = cellHeight / h;
        } else {
            scale = cellWidth / w;
        }

        if (!fillThumbnails) {
            if (scale < 1.0) {
                g2d.scale(scale, scale);
            } else {
                scale = 1.0;
            }
        } else {
            g2d.scale(scale, scale);
        }

        int width = (int) (w * scale);
        int height = (int) (h * scale);

        int x = (int) ((cellWidth - width) / (2 * scale));
        int y = (int) ((cellHeight - height) / (2 * scale));
        g2d.drawImage(image, x, y, null);
        g2d.dispose();

        return img;
    }
}
