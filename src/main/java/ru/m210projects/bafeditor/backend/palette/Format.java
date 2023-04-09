package ru.m210projects.bafeditor.backend.palette;

public enum Format {
    NULL(""), BUILD("dat"), PHOTOSHOP("act"), MICROSOFT("pal");

    private final String ext;
    Format(String ext) {
        this.ext = ext;
    }

    public String getExtension() {
        return ext;
    }

    public static Format getFormat(String extension) {
        switch (extension.toLowerCase()) {
            case "dat":
                return BUILD;
            case "act":
                return PHOTOSHOP;
            case "pal":
                return MICROSOFT;
        }
        return NULL;
    }
}
