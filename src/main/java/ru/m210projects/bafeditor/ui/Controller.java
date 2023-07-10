package ru.m210projects.bafeditor.ui;

import ru.m210projects.bafeditor.UserContext;
import ru.m210projects.bafeditor.backend.filehandler.*;
import ru.m210projects.bafeditor.backend.filehandler.fs.Directory;
import ru.m210projects.bafeditor.backend.filehandler.grp.GrpFile;
import ru.m210projects.bafeditor.backend.palette.Format;
import ru.m210projects.bafeditor.backend.palette.Palette;
import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.components.RadiusButton;
import ru.m210projects.bafeditor.ui.components.TileViewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Controller {

    /**
     * Загрузка конфига
     * Последние открытые файлы
     * Загрузка полатры
     * Сохранение палитры
     * Кастомная палитра
     * Загрузка surface и voxel данных
     * вырезать / копировать тайлы
     * вставить новый тайл, сдвинуть тайл влево / вправо
     * Экпорт тайлов в картинку
     * Импорт тайлов из картинки
     * Отчитстить / удалить тайлы
     * Тайл в буфере-обмена
     * Конвертация тайлов
     * Выпадающее меню по тайлам
     * изменение фона TileCanvas
     * Настройка размеров тайлов в TileBrowser
     * Resize тайлов
     * Импорт def
     * Задание первого тайла в ART файле
     * batch export
     * Zoom ползунок
     */

    private final UserContext userContext = UserContext.getInstance();
    private View view;

    public void onInit(View view) {
        this.view = view;
        onNewArt();
        onChangePalette(new ResourceEntry("blood.act"));

        try {
            GrpFile grp = new GrpFile("duke3d.grp", () -> new FileInputStream("D:\\Temp\\Duke3d.grp"));
            Directory dir = new Directory(Paths.get("D:\\Temp\\Blood\\"));

            EntryGroup gr = new EntryGroup("User");
            for (Entry entry : dir.getEntries()) {
                if (entry.getExtension().equalsIgnoreCase("art")
                        || entry.getExtension().equalsIgnoreCase("dat") ) {
                    gr.add(entry);
                }
            }
            gr.add(new URLEntry(new URL("http://m210.ucoz.ru/Files/Logs/BloodGDX/apr012023205815.log")));

            view.getFileList().updateFileList(gr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onChangePalette(Entry entry) {
        try {
            Palette palette = new Palette(entry, Format.getFormat(entry.getExtension()));
            view.getTileBrowser().setPalette(palette.getModel());
            view.getTileViewer().setPalette(palette.getModel());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onNewArt() {
        ArtFile artFile = new ArtFile("", 0, 256);
        userContext.setArtFile(artFile);
        userContext.setCurrentTile(artFile.getFirstTile());
        view.getTilePropertiesTree().onTileSelected(artFile.getFirstTile());
        view.getTileBrowser().update(artFile);
        view.getTileViewer().repaint();
    }

    public void onLoadArt(Entry item) {
        ArtFile artFile = new ArtFile(item.getName(), item::getInputStream);
        userContext.setArtFile(artFile);
        userContext.setCurrentTile(artFile.getFirstTile());
        view.getTilePropertiesTree().onTileSelected(artFile.getFirstTile());
        view.getTileBrowser().update(artFile);
        view.getTileViewer().repaint();
    }

    public void onEntryClicked(Entry item) {
        if (item.getExtension().equalsIgnoreCase("art")) {
            onLoadArt(item);
        }
        if (item.getExtension().equalsIgnoreCase("dat")) {
            onChangePalette(item);
        }
    }



    // Animation controller

    public void onAnimationTriggerClicked(ActionEvent e) {
        RadiusButton animationTrigger = (RadiusButton) e.getSource();
        animationTrigger.setText("Start");

        TileViewer tileViewer = view.getTileViewer();
        if (tileViewer.isAnimationRunning()) {
            tileViewer.stopAnimation();
        } else {
            tileViewer.startAnimation();
            animationTrigger.setText("Stop");
        }
    }

    // Tile browser controller

    public void onTileSelected(int tile) {
        userContext.setCurrentTile(tile);
        view.getTileViewer().setSelectedTile(tile);
        view.getTilePropertiesTree().onTileSelected(tile);
    }

    public void onTileRangeSelected(int start, int end) {
        userContext.setCurrentTile(start);
    }

    public void onShowPopupMenu(int tile, MouseEvent e) {
        System.out.println("onShowPopupMenu " + tile);
    }

    // Tile viewer controller

    public void onFillButtonClicked(ActionEvent e) {
        ArtEntry pic = userContext.getCurrentEntry();
        TileViewer viewer = view.getTileViewer();
        if (pic.getSize() != 0) {
            Dimension dimension = viewer.getCanvasSize();
            float kt = pic.getWidth() / (float) pic.getHeight();
            double kv = dimension.getWidth() / dimension.getHeight();
            float scale;
            float size;
            if (kv >= kt) {
                size = (float) (dimension.getHeight() / pic.getHeight());
            } else {
                size = (float) (dimension.getWidth() / pic.getWidth());
            }

            if (size >= 1.0) {
                scale = Math.min(size - size % 1, 10.0f);
            } else if (size < 0.25f) {
                scale = 0.25f;
            } else {
                scale = Math.min(size, 10.0f);
            }

            viewer.setScale(scale);
//          zoomSlider.setValue((int) (scale * 100));
            viewer.setCross((int) ((dimension.getWidth() / 2) + (int) (scale * pic.getOffsetX())),
                    (int) ((dimension.getHeight() / 2) + (int) (scale * pic.getOffsetY())));
        }
    }

    public void onResetPositionButtonClicked(ActionEvent e) {
        view.getTileViewer().resetPosition();
    }

    public void onResetZoomButtonClicked(ActionEvent e) {
        view.getTileViewer().setScale(1.0f);
    }

    public void onCrossButtonClicked(ActionEvent e) {
        view.getTileViewer().switchCross();
    }

    public void onPrevContourButtonClicked(ActionEvent e) {
        view.getTileViewer().switchPrevTile();
    }

    public void onNextContourButtonClicked(ActionEvent e) {
        view.getTileViewer().switchNextTile();
    }

}
