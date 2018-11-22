package com.sherlocky.interview.javase.itext.pdf;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * itext 解析 PDF 章节（书签）信息
 */
public class ParsePdfBookMark {
    public static void main(String[] args) {
        String string = "C:/xxxx.pdf";// pdf文件路径
        try {
            inspect(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inspect a PDF file and write the info to a txt file
     *
     * @param writer   Writer to a text file
     * @param filename Path to the PDF file
     * @throws IOException
     */

    public static void inspect(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename); // 读取pdf所使用的输出流
        /**
         * 获取pdf目录信息
         */
        List<HashMap<String, Object>> bms = SimpleBookmark.getBookmark(reader);
        System.out.println(JSON.toJSONString(bms));
        // [{"Action":"GoTo","Page":"1 Fit","Title":"封面"},{"Action":"GoTo","Page":"4 Fit","Title":"书名页"},{"Action":"GoTo","Page":"5 Fit","Title":"版权页"},{"Action":"GoTo","Page":"6 Fit","Title":"前言"},{"Action":"GoTo","Page":"7 Fit","Title":"目录页"}]
        /**
         * Action == "GoTO" 才可以跳转到对应页
         * Page 中的 Fit 要过滤掉
         * Ttile 为目录 名称
         */
    }
}
