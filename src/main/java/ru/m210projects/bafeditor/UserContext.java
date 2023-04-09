package ru.m210projects.bafeditor;

import ru.m210projects.bafeditor.backend.tiles.ArtFile;
import ru.m210projects.bafeditor.ui.models.BloodData;

public class UserContext {

    private UserContext() {
    }

    private static class SingletonHolder {
        public static final UserContext HOLDER_INSTANCE = new UserContext();
    }

    public static UserContext getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private BloodData bloodData;
    private ArtFile artFile;
    private int currentTile = 0;

    public ArtFile getArtFile() {
        return artFile;
    }

    public void setArtFile(ArtFile currentArtFile) {
        this.artFile = currentArtFile;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(int currentTile) {
        this.currentTile = currentTile;
    }

    public BloodData getBloodData() {
        return bloodData;
    }

    public void setBloodData(BloodData bloodData) {
        this.bloodData = bloodData;
    }
}
