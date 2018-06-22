package com.huyistudio.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static final Level ShowLevel = Level.Error;

    private static enum Level {
        Debug, Error
    }

    public static void e(String tag, String msg) {
        formatLog(tag, msg, Level.Error);
    }

    public static void d(String tag, String msg) {
        formatLog(tag, msg, Level.Debug);
    }

    private static void formatLog(String tag, String msg, Level level) {

        if (level.ordinal() < ShowLevel.ordinal()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(formatTime());
        sb.append(" ");
        sb.append(formatLevel(level));
        sb.append("/");
        sb.append(tag);
        sb.append(" ");
        sb.append(msg);

        if (level == Level.Debug) {
            System.out.println(sb.toString());
        } else if (level == Level.Error) {
            System.err.println(sb.toString());
        }
    }

    private static String formatLevel(Level level) {
        switch (level) {
            case Debug:
                return "D";
            case Error:
                return "E";
            default:
                return "V";
        }
    }

    private static String formatTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        return formatter.format(currentTime);
    }
}