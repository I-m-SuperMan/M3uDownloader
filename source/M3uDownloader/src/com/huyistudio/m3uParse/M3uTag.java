package com.huyistudio.m3uParse;

import com.huyistudio.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class M3uTag {

    public String name; //包含#，例如：#EXTM3U ，
    public String valueSignal;  //单个内容: #EXT-X-VERSION:4 . 这个就为4.
    public Map<String, String> keyValue;//map内容 ， 比如 #EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="subs",NAME="中文",LANGUAGE="zho",URI="GEPH-ONTHERECS02E017C-_E17101101_master_cc_zho_x65a8c522b64140e18e890c5e5791434f.m3u8"
    public String uri;//可能为空，比如：#EXTM3U 这些标签就没有

    public static M3uTag parseTag(String content, M3uTag prevTag) {
        if (TextUtils.isEmpty(content)) {
            return prevTag;
        }

        M3uTag newTag = new M3uTag();

        if (content.startsWith("#")) {
            newTag.parseLabel(content);
        } else {
            //不是#开头的，说明是uri。这个时候就要用到上一个tag了。
            if (prevTag == null) {
                throw new IllegalStateException("prev tag Can't be null!! code wrong?");
            }
            prevTag.uri = content;
        }

        return newTag;
    }

    private void parseLabel(String content) {
        if (!content.contains(":")) {
            //没有值,整个就是标签了。
            name = content;
        } else {
            //有冒号， 说明是有值的。
            name = content.substring(0, content.indexOf(":"));

            String value = content.substring(content.indexOf(":"));
            if (value.contains("=")) {
                //map形式
                keyValue = getValueMap(value);

            } else {
                //单个值
                if (value.endsWith(",")) {
                    //比如这样的：#EXTINF:10.000000,
                    valueSignal = value.substring(0, value.length() - 1);
                } else {
                    valueSignal = value;
                }
            }
        }
    }

    private Map<String, String> getValueMap(String valueStr) {

        Map<String, String> keyValue = new HashMap<String, String>();
        String[] splits = valueStr.split("=");
        int len = splits.length;
        String key = null;
        String value = null;
        for (int i = 0; i < len; i++) {
            //头
            if (i == 0) {
                key = splits[i];
                continue;
            }
            //尾
            if (i == len - 1) {
                value = removeQuat(splits[i]);
                keyValue.put(key, value);
                continue;//实际上就结束了
            }
            //中间
            int douhaoIndex = splits[i].lastIndexOf(",");

            String prevValue = splits[i].substring(0, douhaoIndex);
            prevValue = removeQuat(prevValue);
            keyValue.put(key, prevValue);

            String nextKey = splits[i].substring(douhaoIndex + 1);
            key = nextKey;
        }

        return keyValue;
    }

    private String removeQuat(String str) {
        if (str.startsWith("\"")) {
            return str.substring(1, str.length() - 1);
        } else {
            return str;
        }
    }

}
