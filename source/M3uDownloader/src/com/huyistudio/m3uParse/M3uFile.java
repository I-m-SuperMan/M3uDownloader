package com.huyistudio.m3uParse;

import com.huyistudio.util.TextUtils;

import java.util.LinkedList;

public class M3uFile {

    public LinkedList<M3uTag> tagLinkedList;

    private String mLocation;
    private String mSaveDirPath;

    public M3uFile(String location, String saveDirPath) {

        mLocation = location;
        mSaveDirPath = saveDirPath;

        tagLinkedList = new LinkedList<M3uTag>();
    }

    public void parse(String lineStr) {
        M3uTag prevTag = null;
        if (!tagLinkedList.isEmpty()) {
            prevTag = tagLinkedList.getLast();
        }

        M3uTag tag = M3uTag.parseTag(lineStr, prevTag);
        if (tag != prevTag) {
            tagLinkedList.add(tag);
        } else {
            //相等，可能是null，可能是uri的tag。
        }
    }

    public void download() {
        //下载文件中的其他的m3u或者ts
        for (M3uTag tag : tagLinkedList) {
            if (TextUtils.isEmpty(tag.uri)) {
                //没有uri，看看map中有没有
                if (tag.keyValue != null && tag.keyValue.containsKey("URI")) {
                    String uri = tag.keyValue.get("URI");
                    downloadUri(uri);
                }
            } else {
                downloadUri(tag.uri);
            }
        }
    }

    private void downloadUri(String uri) {

        String filePath = uri;
        if (filePath.contains("?")) {
            filePath = filePath.substring(0, filePath.indexOf("?"));
        }

        //有的话，判断是ts还是m3u
        if (filePath.endsWith(".ts") || filePath.endsWith(".aac")) {
            //下载媒体数据
            downloadBinary(uri);
        } else if (filePath.endsWith(".m3u") || filePath.endsWith(".m3u8")) {
            //下载M3u..
            downloadM3u(uri);
        } else if (filePath.endsWith(".vtt")) {
            //下载vtt..
            downloadVtt(uri);
        } else {
            //TODO 其他格式。。。。
        }
    }

    private void downloadVtt(String uri) {
        VttTxtDownloader vttTxtDownloader = new VttTxtDownloader(mLocation, uri, mSaveDirPath);
        vttTxtDownloader.start();
    }

    private void downloadM3u(String uri) {
        M3uTxtDownloader m3uTxtDownloader = new M3uTxtDownloader(mLocation, uri, mSaveDirPath);
        m3uTxtDownloader.start();
    }

    private void downloadBinary(String uri) {
        M3uBinaryDownloader binaryDownloader = new M3uBinaryDownloader(mLocation, uri, mSaveDirPath);
        binaryDownloader.start();
    }
}
