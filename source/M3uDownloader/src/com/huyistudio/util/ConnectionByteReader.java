package com.huyistudio.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionByteReader implements ConnectionReader<byte[]> {

    private static final String TAG = ConnectionByteReader.class.getSimpleName();

    public static final int MAX_BUFFER_SIZE = 1024 * 50;

    private String mUrl = null;
    private boolean openSuccess = false;

    private URLConnection connection = null;
    private InputStream inputStream = null;

    public ConnectionByteReader(String url) {
        mUrl = url;
    }

    @Override
    public boolean open() {
        openSuccess = false;
        try {
            URL uri = new URL(mUrl);
            connection = uri.openConnection();
            int responseCode = -1;
            if (mUrl.startsWith("http://")) {
                HttpURLConnection conn = (HttpURLConnection) connection;
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                conn.connect();
                responseCode = conn.getResponseCode();
            } else if (mUrl.startsWith("https://")) {
                HttpsURLConnection conn = (HttpsURLConnection) connection;
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                conn.connect();
                responseCode = conn.getResponseCode();
            }

            if (responseCode != 200) {
                //不是200，我们都认为是有问题的。。
                return false;
            }

            inputStream = connection.getInputStream();

        } catch (Exception e) {
            return false;
        }

        openSuccess = true;
        return true;
    }

    @Override
    public byte[] read() {
        if (openSuccess) {
            try {
                byte[] b = new byte[MAX_BUFFER_SIZE];
                int i = inputStream.read(b);
                if (i != -1) {

                    if (i < MAX_BUFFER_SIZE) {
                        Log.e(TAG, "read Not enough...");
                        byte[] b1 = new byte[i];
                        System.arraycopy(b, 0, b1, 0, i);
                        return b1;
                    } else {
                        return b;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        throw new IllegalStateException("open failed? or not open?");
    }

    @Override
    public void close() {
        openSuccess = false;
        closeInputStream(inputStream);
        closeConnection(connection);
    }


    private void closeConnection(URLConnection connection) {
        if (connection != null) {
            if (mUrl.startsWith("http://")) {
                HttpURLConnection conn = (HttpURLConnection) connection;
                conn.disconnect();
            } else if (mUrl.startsWith("https://")) {
                HttpsURLConnection conn = (HttpsURLConnection) connection;
                conn.disconnect();
            }
        }
        connection = null;
    }

    private void closeInputStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = null;
    }
}
