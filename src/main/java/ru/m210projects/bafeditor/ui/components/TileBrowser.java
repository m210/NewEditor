package ru.m210projects.bafeditor.ui.components;

import ru.m210projects.bafeditor.backend.tiles.ArtFile;

import java.awt.*;

public class TileBrowser extends TileCanvas {

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle view = this.getVisibleRect();
        ShadowUtils.drawRectShadow(g, 0, 0, view.width - 1, view.height - 1, true);
    }

    public void update(ArtFile file) {

    }
}
