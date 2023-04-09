package ru.m210projects.bafeditor;

import ru.m210projects.bafeditor.backend.tiles.ArtEntry;
import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.models.BloodData;

import static ru.m210projects.bafeditor.backend.tiles.ArtFile.DUMMY_ENTRY;

public class UserContext {

    private BloodData bloodData;
    private ArtFile artFile;
    private int currentTile = 0;

    private UserContext() {
    }

    public static UserContext getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public void setArtFile(ArtFile currentArtFile) {
        this.artFile = currentArtFile;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    public ArtEntry getCurrentEntry() {
        if (artFile != null) {
            return artFile.getEntry(currentTile);
        }
        return DUMMY_ENTRY;
    }

    public ArtEntry getArtEntry(int tile) {
        if (artFile != null) {
            return artFile.getEntry(tile);
        }
        return DUMMY_ENTRY;
    }

    public void setCurrentTile(int currentTile) {
        this.currentTile = currentTile;
    }

    public int getCurrentTileIndex() {
        return currentTile - artFile.getFirstTile();
    }

    public BloodData getBloodData() {
        return bloodData;
    }

    public void setBloodData(BloodData bloodData) {
        this.bloodData = bloodData;
    }

    private static class SingletonHolder {
        public static final UserContext HOLDER_INSTANCE = new UserContext();
    }
}
