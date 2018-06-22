package com.huyistudio.m3uParse;

import com.huyistudio.downloader.BinaryDownloader;
import com.huyistudio.util.*;

public class M3uBinaryDownloader extends BinaryDownloader {

    private static final String TAG = M3uBinaryDownloader.class.getSimpleName();

    public M3uBinaryDownloader(String location, String filePath, String saveDirPath) {
        super(location, filePath, saveDirPath);
    }

    @Override
    protected String getSaveDirPath() {
        String finalSaveDirPath = null;
        if (mOutSetFilePath.contains("/")) {
            //需要创建子目录
            String childDirPath = mOutSetFilePath.substring(0, mOutSetFilePath.lastIndexOf("/"));
            String maybeFinalDirPath = mOutSetSaveDirPath + childDirPath;
            boolean chidDirExit = FileUtils.createDir(maybeFinalDirPath);
            if (!chidDirExit) {
                //结束了。。。
            } else {
                finalSaveDirPath = SeparatorUtils.addEnd(maybeFinalDirPath);
            }
        } else {
            //不需要，保存到saveDirPath就行了
            finalSaveDirPath = mOutSetSaveDirPath;

        }
        return finalSaveDirPath;
    }

    @Override
    protected String getSaveFileName() {
        String finalBinaryName = mOutSetFilePath;
        if (mOutSetFilePath.contains("/")) {
            finalBinaryName = finalBinaryName.substring(mOutSetFilePath.lastIndexOf("/"));
        }

        if (finalBinaryName.contains("?")) {
            //鉴权信息等。。忽略掉
            finalBinaryName = finalBinaryName.substring(0, finalBinaryName.indexOf("?"));
        }

        return finalBinaryName;
    }

    @Override
    protected String getUrl() {
        //TODO 如果filePath是http开头的话，这就有问题了。
        return mOutSetLocation + mOutSetFilePath;
    }

    FileByteWriter fileBufferWriter = null;

    @Override
    protected void onReadStart() {
        String filePath = getSaveDirPath() + getSaveFileName();
        fileBufferWriter = new FileByteWriter(filePath);
    }

    @Override
    protected void onReadResult(byte[] buffer) {
        if (buffer != null) {
            fileBufferWriter.write(buffer);
        }
    }

    @Override
    protected void onReadEnd() {
        fileBufferWriter.close();
    }

}