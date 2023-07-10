package ru.m210projects.bafeditor.ui;

import ru.m210projects.bafeditor.ui.components.TileBrowser;
import ru.m210projects.bafeditor.ui.components.TileViewer;
import ru.m210projects.bafeditor.ui.components.filelist.FileListPanel;
import ru.m210projects.bafeditor.ui.components.iconbar.IconBarPanel;
import ru.m210projects.bafeditor.ui.components.tileproperties.TilePropertiesTree;

public class View {

    private final TileBrowser tileBrowser;
    private final TileViewer tileViewer;
    private final FileListPanel fileList;
    private final TilePropertiesTree tilePropertiesTree;
    private final IconBarPanel iconBarPanel;
    private final Controller controller;

    public View(Controller controller) {
        this.controller = controller;

        this.tileBrowser = new TileBrowser(controller);
        this.tileViewer = initTileViewer();

        this.fileList = initFileList();
        this.tilePropertiesTree = initTilePropTree();
        this.iconBarPanel = initIconBar();
        this.controller.onInit(this);
    }

    private IconBarPanel initIconBar() {
        return new IconBarPanel();
    }

    private TilePropertiesTree initTilePropTree() {
        TilePropertiesTree tilePropertiesTree = new TilePropertiesTree(controller);
        tilePropertiesTree.setToggleClickCount(1);
        tilePropertiesTree.setOpaque(false);
        return tilePropertiesTree;
    }

    private FileListPanel initFileList() {
        FileListPanel fileListPanel = new FileListPanel();
        fileListPanel.setEntryClickListener(controller::onEntryClicked);
        return fileListPanel;
    }

    private TileViewer initTileViewer() {
        return new TileViewer(controller);
    }

    public TileBrowser getTileBrowser() {
        return tileBrowser;
    }

    /**
     * @return The panel with selected tile canvas
     */
    public TileViewer getTileViewer() {
        return tileViewer;
    }

    public FileListPanel getFileList() {
        return fileList;
    }

    public TilePropertiesTree getTilePropertiesTree() {
        return tilePropertiesTree;
    }

    public IconBarPanel getIconBarPanel() {
        return iconBarPanel;
    }

    public void dispose() {

    }
}
