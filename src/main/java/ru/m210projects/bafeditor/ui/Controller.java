package ru.m210projects.bafeditor.ui;

import ru.m210projects.bafeditor.backend.filehandler.*;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.components.RadiusButton;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Controller {

    private View view;

    public void onInit(View view) {
        this.view = view;

        try {
            Entry palette = new ResourceEntry("blood.act");
            Directory dir = new Directory(Paths.get("D:\\Temp\\Blood\\"));

            EntryGroup gr = new EntryGroup("User");
            for (Entry entry : dir.getEntries()) {
                if (entry.getExtension().equals("art")) {
                    gr.add(entry);
                }
            }
            gr.add(palette);
            gr.add(new URLEntry(new URL("http://m210.ucoz.ru/Files/Logs/BloodGDX/apr012023205815.log")));

            view.getFileList().updateFileList(gr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEntryClicked(Entry item) {
        ArtFile artFile = new ArtFile(item.getName(), item::getInputStream);

        // update TileBrowser
        // browser will update
        // -> update TileViewer
        // -> update TileProps

        System.out.println(item + " " + artFile.getSize());
    }

    // Animation controller

    boolean enabled = true;
    public void onAnimationTriggerClicked(ActionEvent e) {
        System.out.println("onAnimationButtonClicked");
        enabled = !enabled;
        RadiusButton animationTrigger = (RadiusButton) e.getSource();
        animationTrigger.setText(enabled ? "Stop" : "Start");
    }

    // Tile viewer controller

    public void onFillButtonClicked(ActionEvent e) {
        System.out.println("onFillButtonClicked");
    }

    public void onResetPositionButtonClicked(ActionEvent e) {
        System.out.println("onResetPositionButtonClicked");
    }

    public void onResetZoomButtonClicked(ActionEvent e) {
        System.out.println("onResetZoomButtonClicked");
    }

    public void onCrossButtonClicked(ActionEvent e) {
        System.out.println("onCrossButtonClicked");
    }

    public void onPrevContourButtonClicked(ActionEvent e) {
        System.out.println("onPrevContourButtonClicked");
    }

    public void onNextContourButtonClicked(ActionEvent e) {
        System.out.println("onNextContourButtonClicked");
    }

}
