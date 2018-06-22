package com.huyistudio.util;

import java.io.File;

/**
 * 斜杠处理工具
 */
public class SeparatorUtils {
    public static String removeEnd(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        String finalStr = source;
        while (finalStr.endsWith(File.separator)) {
            finalStr = finalStr.substring(0, finalStr.length() - 1);
        }
        return finalStr;
    }

    public static String removeHead(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        String finalStr = source;
        while (finalStr.startsWith(File.separator)) {
            finalStr = finalStr.substring(1, finalStr.length());
        }
        return finalStr;
    }

    public static String addHead(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        String finalStr = source;
        if (!finalStr.startsWith(File.separator)) {
            finalStr = File.separator + finalStr;
        }
        return finalStr;
    }

    public static String addEnd(String source) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        String finalStr = source;
        if (!finalStr.endsWith(File.separator)) {
            finalStr = finalStr + File.separator;
        }
        return finalStr;
    }
}

