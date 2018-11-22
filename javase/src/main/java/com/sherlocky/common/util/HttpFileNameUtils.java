package com.sherlocky.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class HttpFileNameUtils {
    /**
     * 中文编码转换, 例如：下载文件文件名包含中文时, 部分浏览器会乱码
     */
    public static String convert(HttpServletRequest request, String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("文件名称参数为 null");
        }
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("Trident") || -1 != agent.indexOf("Edge"))) {
                return URLEncoder.encode(fileName, "UTF8").replace("+", "%20");
            } else if (null != agent && -1 != agent.indexOf("Safari")) {
                return new String(fileName.getBytes("utf-8"), "ISO8859-1");
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                // 此处使用Java8 原生的 Base64 类
                return "=?UTF-8?B?" + (new String(Base64.getEncoder().encodeToString(fileName.getBytes("UTF-8")))) + "?=";
            } else {
                return fileName;
            }
        } catch (UnsupportedEncodingException ex) {
            return fileName;
        }
    }
}
