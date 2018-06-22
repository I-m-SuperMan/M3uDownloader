package com.huyistudio.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
    public static String getLocation(String url) {
        String location = null;
        Pattern hostPattern = Pattern.compile("([a-zA-Z]+://[^/]+).*[/]");
        Matcher matcher = hostPattern.matcher(url);
        if (matcher.find()) {
            location = matcher.group(0);
        }
        return location;
    }

    public static String getFilePath(String url) {
        String location = getLocation(url);
        return url.substring(location.length());
    }

}