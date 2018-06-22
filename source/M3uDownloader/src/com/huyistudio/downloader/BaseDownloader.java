package com.huyistudio.downloader;

import com.huyistudio.util.FileUtils;
import com.huyistudio.util.Log;
import com.huyistudio.util.SeparatorUtils;
import com.huyistudio.util.TextUtils;

public class BaseDownloader {

    private static final String TAG = BaseDownloader.class.getSimpleName();

    public BaseDownloader(String location, String filePath, String saveDirPath) {
        init(location, filePath, saveDirPath);
    }

    private void init(String location, String filePath, String saveDirPath) {
        Log.d(TAG, "location = " + location + " , filePath = " + filePath);

        if (TextUtils.isEmpty(location)) {
            throw new IllegalArgumentException("location is Empty !");
        }

        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath is Empty !");
        }

        if (TextUtils.isEmpty(saveDirPath)) {
            throw new IllegalArgumentException("saveDirPath is Empty !");
        }

        boolean dirExits = FileUtils.createDir(saveDirPath);
        if (!dirExits) {
            throw new IllegalArgumentException("saveDir cannot create : " + saveDirPath);
        }

        mOutSetLocation = SeparatorUtils.addEnd(location);
        mOutSetFilePath = SeparatorUtils.removeHead(filePath);
        mOutSetSaveDirPath = SeparatorUtils.addEnd(saveDirPath);
    }

    protected String mOutSetLocation = null;
    protected String mOutSetFilePath = null;
    protected String mOutSetSaveDirPath = null;
}
