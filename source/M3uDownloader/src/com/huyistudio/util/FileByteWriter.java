package com.huyistudio.util;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileByteWriter {
    FileOutputStream fwriter = null;

    public FileByteWriter(String path) {
        try {
            fwriter = new FileOutputStream(path);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void write(byte[] content) {
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
