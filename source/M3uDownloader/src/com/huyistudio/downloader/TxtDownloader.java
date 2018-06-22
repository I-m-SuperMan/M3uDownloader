package com.huyistudio.downloader;

import com.huyistudio.util.ConnectionLineReader;
import com.huyistudio.util.FileUtils;
import com.huyistudio.util.Log;
import com.huyistudio.util.TextUtils;

public abstract class TxtDownloader extends BaseDownloader {

    private static final String TAG = TxtDownloader.class.getSimpleName();

    public TxtDownloader(String location, String filePath, String saveDirPath) {
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
        ConnectionLineReader connectionLineReader = new ConnectionLineReader(url);
        do {
            boolean openSuccess = connectionLineReader.open();
            if (!openSuccess) {
                break;
            }

            onReadStart();

            String lineStr = null;
            do {
                lineStr = connectionLineReader.read();
                Log.d(TAG, lineStr);

                onReadResult(lineStr);

            } while (lineStr != null);

        } while (false);
        connectionLineReader.close();

        onReadEnd();
    }


    protected abstract String getSaveDirPath();

    protected abstract String getSaveFileName();

    protected abstract String getUrl();

    protected abstract void onReadStart();

    protected abstract void onReadResult(String content);

    protected abstract void onReadEnd();

}
