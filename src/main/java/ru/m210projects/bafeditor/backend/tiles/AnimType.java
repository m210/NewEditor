package ru.m210projects.bafeditor.backend.tiles;

public enum AnimType {
    OSCIL, FORWARD, BACKWARD, NONE;

    public static AnimType get(int picanm) {
        switch (picanm & 192) {
            case 64:
                return AnimType.OSCIL;
            case 128:
                return AnimType.FORWARD;
            case 192:
                return AnimType.BACKWARD;
        }
        return AnimType.NONE;
    }
}