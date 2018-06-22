package com.huyistudio.downloader;

import com.huyistudio.util.*;

public abstract class BinaryDownloader extends BaseDownloader{

    private static final String TAG = BinaryDownloader.class.getSimpleName();

    public BinaryDownloader(String location, String filePath, String saveDirPath) {
        super(location, filePath, saveDirPath);
    }

    public void start() {
        String finalSaveDirPath = getSaveDirPath();
        if (TextUtils.isEmpty(finalSaveDirPath)) {
            return;
        }

        String finalM3uName = getSaveFileName();
        if (TextUtils.isEmpty(finalM3uName)) {
            return;
        }

        boolean fileExits = FileUtils.createFile(finalSaveDirPath, finalM3uName);
        if (!fileExits) {
            return;
        }

        String url = getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Log.e(TAG, "开始下载：" + url);
        ConnectionByteReader connectionByteReader = new ConnectionByteReader(url);
        do {
            boolean openSuccess = connectionByteReader.open();
            if (!openSuccess) {
                break;
            }

            onReadStart();

            byte[] buffer = null;
            do {
                buffer = connectionByteReader.read();
                onReadResult(buffer);

            } while (buffer != null);

        } while (false);
        connectionByteReader.close();

        onReadEnd();
    }


    protected abstract String getSaveDirPath();

    protected abstract String getSaveFileName();

    protected abstract String getUrl();

    protected abstract void onReadStart();

    protected abstract void onReadResult(byte[] content);

    protected abstract void onReadEnd();
}