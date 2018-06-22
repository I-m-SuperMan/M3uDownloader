package com.huyistudio.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileStringWriter {
    FileWriter fwriter = null;

    public FileStringWriter(String path) {
        try {
            fwriter = new FileWriter(path);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void write(String content) {
        try {
            fwriter.write(content);
            fwriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            fwriter.close();
            fwriter = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
