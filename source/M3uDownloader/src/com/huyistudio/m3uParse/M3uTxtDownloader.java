package com.huyistudio.m3uParse;

import com.huyistudio.downloader.TxtDownloader;
import com.huyistudio.util.*;

public class M3uTxtDownloader extends TxtDownloader {

    private static final String TAG = M3uTxtDownloader.class.getSimpleName();

    private FileStringWriter fileStringWriter = null;
    private M3uFile m3uFile = null;

    public M3uTxtDownloader(String location, String filePath, String saveDirPath) {
        super(location, filePath, saveDirPath);
    }

    @Override
    protected String getSaveDirPath() {

        String finalSaveDirPath = null;
        if (mOutSetFilePath.contains("/")) {
            //需要创建子目录
            String childDirPath = mOutSetFilePath.substring(0, mOutSetFilePath.lastIndexOf("/"));
            String maybeFinalDirPath = mOutSetSaveDirPath + childDirPath;
            boolean childDirExit = FileUtils.createDir(maybeFinalDirPath);
            if (!childDirExit) {
                //创建失败了。。。
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
        String finalM3uName = mOutSetFilePath;
        if (mOutSetFilePath.contains("/")) {
            finalM3uName = finalM3uName.substring(mOutSetFilePath.lastIndexOf("/"));
        }

        if (finalM3uName.contains("?")) {
            //鉴权信息等。。忽略掉
            finalM3uName = finalM3uName.substring(0, finalM3uName.indexOf("?"));
        }

        return finalM3uName;
    }

    @Override
    protected String getUrl() {
        //TODO 如果filePath是http开头的话，这就有问题了。
        return mOutSetLocation + mOutSetFilePath;
    }

    @Override
    protected void onReadStart() {
        String filePath = getSaveDirPath() + getSaveFileName();
        fileStringWriter = new FileStringWriter(filePath);
        m3uFile = new M3uFile(UrlUtil.getLocation(getUrl()), getSaveDirPath());
    }

    @Override
    protected void onReadResult(String lineStr) {
        m3uFile.parse(lineStr);
        if (lineStr != null) {
            fileStringWriter.write(lineStr + "\r\n");//一行，加上回车
            //TODO 如果是http的路径，不是相对路径，我该怎么存到txt中？
        }
    }

    @Override
    protected void onReadEnd() {
        fileStringWriter.close();
        m3uFile.download();
    }

}
