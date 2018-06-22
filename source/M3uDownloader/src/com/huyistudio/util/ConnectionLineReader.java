package com.huyistudio.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionLineReader implements ConnectionReader<String> {

    private String mUrl = null;
    private boolean openSuccess = false;

    private URLConnection connection = null;
    private InputStream inputStream = null;
    private InputStreamReader inputStreamReader = null;
    private BufferedReader reader = null;

    public ConnectionLineReader(String url) {
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
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

        } catch (Exception e) {
            return false;
        }

        openSuccess = true;
        return true;
    }

    @Override
    public String read() {
        if (openSuccess) {
            try {
                return reader.readLine();
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
        closeInputStream(inputStream, inputStreamReader, reader);
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

    private void closeInputStream(InputStream inputStream, InputStreamReader inputStreamReader, BufferedReader reader) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = null;
        inputStreamReader = null;
        reader = null;
    }
}
