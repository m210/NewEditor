package ru.m210projects.bafeditor.backend.filehandler;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamProvider {
    InputStream newInputStream() throws IOException;

}