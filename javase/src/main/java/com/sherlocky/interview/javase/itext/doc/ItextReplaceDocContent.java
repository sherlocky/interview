package com.sherlocky.interview.javase.itext.doc;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 测试itext替换word 内容
 */
public class ItextReplaceDocContent {
    public static void main(String[] args) {
        FileInputStream input = null;
        String result = "";
        try {
            input = new FileInputStream("F:/testItextReplace.doc");
            Document document = new Document(PageSize.A4);
            document.newPage();
//			WordExtractor extractor = new WordExtractor(input);
//			result = extractor.getText();
//			System.out.println(result);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
