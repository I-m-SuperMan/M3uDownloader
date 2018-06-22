package com.huyistudio;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String m3uUrl = "http://www.streambox.fr/playlists/test_001/stream.m3u8";
        String saveDirPath = "/Users/A/Documents/testSaveDir/";
        M3uDownloader m3uTxtDownloader = new M3uDownloader(m3uUrl , saveDirPath);
        if(m3uTxtDownloader.prepare()) {
       		m3uTxtDownloader.start();
    	}
    }
}
