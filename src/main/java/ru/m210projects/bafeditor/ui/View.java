package ru.m210projects.bafeditor.ui;

import ru.m210projects.bafeditor.backend.filehandler.*;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.components.TileBrowser;
import ru.m210projects.bafeditor.ui.components.TileViewer;
import ru.m210projects.bafeditor.ui.components.filelist.FileListPanel;
import ru.m210projects.bafeditor.ui.components.filelist.onEntryClickListener;
import ru.m210projects.bafeditor.ui.components.tileproperties.TilePropertiesTree;
import ru.m210projects.bafeditor.ui.models.TileContainer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class View {

    private final TileBrowser tileBrowser;
    private final TileViewer tileViewer;
    private final FileListPanel fileListPanel;
    private final TilePropertiesTree tilePropertiesTree;

    public View() {
        this.tileBrowser = new TileBrowser();
        this.tileViewer = new TileViewer();
        this.fileListPanel = new FileListPanel();
        this.tilePropertiesTree = new TilePropertiesTree();

        initTilePropTree();

        Entry palette = new ResourceEntry("blood.act");
        try {
            Directory dir = new Directory(Paths.get("D:\\Temp\\Blood\\"));

            EntryGroup gr = new EntryGroup("User");
            for (Entry entry : dir.getEntries()) {
                if (entry.getExtension().equals("art")) {
                    gr.add(entry);
                }
            }
            gr.add(palette);
            gr.add(new URLEntry(new URL("http://m210.ucoz.ru/Files/Logs/BloodGDX/apr012023205815.log")));

            fileListPanel.updateFileList(gr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileListPanel.setEntryClickListener(new onEntryClickListener() {
            @Override
            public void onEntryClicked(Entry item) {
                ArtFile artFile = new ArtFile(item.getName(), item::getInputStream);

                // update TileBrowser
                // browser will update
                // -> update TileViewer
                // -> update TileProps

                System.out.println(item + " " + artFile.getSize());
            }
        });
    }

    private void initTilePropTree() {
        this.tilePropertiesTree.setToggleClickCount(1);
        this.tilePropertiesTree.update(new TileContainer());
        this.tilePropertiesTree.setOpaque(false);
    }

    public TileBrowser getTileBrowser() {
        return tileBrowser;
    }

    public TileViewer getTileViewer() {
        return tileViewer;
    }

    public FileListPanel getFileListPanel() {
        return fileListPanel;
    }

    public TilePropertiesTree getTilePropertiesTree() {
        return tilePropertiesTree;
    }
}
