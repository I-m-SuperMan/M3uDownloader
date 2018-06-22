package com.huyistudio.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static boolean createDir(String dirPath) {
        boolean dirExits = false;
        do {
            File file = new File(dirPath);
            if (file.exists() && file.isDirectory()) {
                dirExits = true;
                break;
            }

            dirExits = file.mkdirs();
        } while (false);

        return dirExits;
    }

    public static boolean createFile(String parent, String child) {
        boolean parentFile = createDir(parent);
        if (!parentFile) {
            return false;
        }

        File file = new File(parent, child);

        if (file.exists() && file.isFile()) {
            return true;
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
