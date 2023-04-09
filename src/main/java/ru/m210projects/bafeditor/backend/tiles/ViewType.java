package ru.m210projects.bafeditor.backend.tiles;

public enum ViewType {

    SINGLE, VIEW5, VIEW8, FLAT, VOXEL, SPIN_VOXEL;

    public static ViewType get(int picanm) {
        switch (((picanm >> 24) & 0x70)) {
            case 16:
                return ViewType.VIEW5;
            case 32:
                return ViewType.VIEW8;
            case 48:
                return ViewType.FLAT;
            case 96:
                return ViewType.VOXEL;
            case 112:
                return ViewType.SPIN_VOXEL;
        }

        return ViewType.SINGLE;
    }
}
