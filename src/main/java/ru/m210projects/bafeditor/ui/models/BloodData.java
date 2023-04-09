package ru.m210projects.bafeditor.ui.models;

import java.io.File;
import java.util.List;

public class BloodData {

    public final String[] surfNames = {"None", "Stone", "Metal", "Wood", "Flesh", "Water", "Dirt", "Clay", "Snow", "Ice", "Leaves", "Cloth", "Plant", "Goo", "Lava"};
    public List<Integer> surfaces;
    public List<Integer> voxels;
    public File surfFile;
    public File voxFile;

    public String getSurfaceName(int tile) {
        if (surfaces != null && tile < surfaces.size()) {
            int surfaceType = surfaces.get(tile);
            if (surfaceType != 0) {
                return surfNames[surfaceType];
            }
        }
        return "";
    }

    public String getVoxelId(int tile) {
        if (voxels != null && tile < voxels.size()) {
            return"Vox: id" + voxels.get(tile);
        }
        return "";
    }


}
