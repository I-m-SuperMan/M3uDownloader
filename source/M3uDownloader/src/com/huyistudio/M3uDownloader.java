package com.huyistudio;

import com.huyistudio.m3uParse.M3uTxtDownloader;
import com.huyistudio.util.*;

/**
 * M3u文件下载器
 * 下载到本地，并保持原来的结构不变
 */
public class M3uDownloader {

    private static final String TAG = M3uDownloader.class.getSimpleName();
    public String mM3uUrl = ""; //原始的m3u地址
    public String mSaveDirPath = ""; //保存的绝对路径


    private boolean mPrepareSuccess = false;//

    public M3uDownloader(String m3uUrl, String saveDirPath) {
        mM3uUrl = m3uUrl;
        mSaveDirPath = saveDirPath;

        checkInputArgsOrThrow();
    }

    private void checkInputArgsOrThrow() {

        if (TextUtils.isEmpty(mM3uUrl)) {
            throw new IllegalArgumentException("m3uUrl is empty");
        }

        if (TextUtils.isEmpty(mSaveDirPath)) {
            throw new IllegalArgumentException("saveDirPath is empty");
        }
    }

    public boolean prepare() {
        mPrepareSuccess = false;
        ConnectionLineReader connectionReader = null;

        do {

            boolean saveDirExits = FileUtils.createDir(mSaveDirPath);
            if (!saveDirExits) {
                Log.e(TAG , "saveDir cannot create : " + mSaveDirPath);
                mPrepareSuccess = false;
                break;
            }

            connectionReader = new ConnectionLineReader(mM3uUrl);
            boolean openSuccess = connectionReader.open();
            if (openSuccess == false) {
                Log.e(TAG ,"m3uUrl cannot connect : " + mM3uUrl);
                mPrepareSuccess = false;
                break;
            }

            String firstLineStr = connectionReader.read();
            boolean isM3uFile = firstLineStr != null && firstLineStr.startsWith("#EXTM3U");
            if (!isM3uFile) {
                Log.e(TAG ,"m3uUrl is not m3u file : " + mM3uUrl);
                mPrepareSuccess = false;
                break;
            }

            mPrepareSuccess = true;
        }
        while (false);

        if (connectionReader != null) {
            connectionReader.close();
        }

        return mPrepareSuccess;
    }

    public void start() {
        if (!mPrepareSuccess) {
            return;
        }
        String location = UrlUtil.getLocation(mM3uUrl);
        String filePath = UrlUtil.getFilePath(mM3uUrl);

        M3uTxtDownloader m3uTxtDownloader = new M3uTxtDownloader(location, filePath , mSaveDirPath);
        m3uTxtDownloader.start();
    }

}
