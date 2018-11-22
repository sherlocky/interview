package com.sherlocky.interview.javase.itext.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试 itxt 合并 pdf文件
 */
public class ItextMergePdf {
    private static Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+");

    public static void main(String[] args) {
        String basePath = "D:/books/";
        File base = new File(basePath);
        File[] fs = base.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                String name = fs[i].getAbsolutePath();
                int idx = name.lastIndexOf(".");
                String folderName = name.substring(0, idx);
                System.out.println(folderName + "\\" + fs[i].getName());
                new File(folderName + "\\").mkdir();
                fs[i].renameTo(new File(folderName + "\\" + fs[i].getName()));
            }
        }
        System.out.println("done!");
    }

    public static void cutBookIdFromPath(String[] args) {
        String basePath = "F:/开发_测试_目录/XXXX/";
        File base = new File(basePath);
        File[] fs = base.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {
                String fileName = fs[i].getName();
                System.out.println(fileName);
                Matcher m = NUMBER_PATTERN.matcher(fileName);
                if (m.find()) {
                    String bookClassId = m.group();
                    System.out.println(bookClassId);
                }
            }
        }
    }

    public static void mergePdf4Test() {
        String basePath = "F:/开发_测试_目录/merge_dest/";
        File base = new File(basePath);
        File[] fs = base.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (!fs[i].isDirectory() || fs[i].listFiles().length == 0) {
                continue;
            }
            File[] pdfs = fs[i].listFiles();
            List<String> pdfPaths = new ArrayList<String>();
            for (int j = 0; j < pdfs.length; j++) {
                pdfPaths.add(pdfs[j].getAbsolutePath());
            }
            Collections.sort(pdfPaths, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    if (s1 == null || s2 == null) {
                        return 0;
                    }
                    if (s1.length() < s2.length()) {
                        return -1;
                    }
                    if (s1.length() > s2.length()) {
                        return 1;
                    }
                    return s1.compareTo(s2);
                }
            });
            System.out.println(pdfPaths);
            String dest = basePath + fs[i].getName() + ".pdf";
            System.out.println(dest);
            mergePdfFiles(pdfPaths.toArray(new String[0]), dest);
        }
    }

    /**
     * @param files   要合并的文件路径数组(绝对路径 如：{ "e:\\1.pdf", "e:\\2.pdf", "e:\\3.pdf"})
     * @param newfile 合并后新产生的文件绝对路径
     * @return boolean 合并成功返回 true
     * @author Sherlocky
     * @date 2016年7月27日
     * @since
     */
    public static boolean mergePdfFiles(String[] files, String newfile) {
        boolean retValue = false;
        Document document = null;
        try {
            document = new Document(new PdfReader(files[0]).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
            document.open();
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);
                /**
                 * 解决Exception in thread "main"
                 * java.lang.IllegalArgumentException: PdfReader not opened with
                 * owner password
                 */
                java.lang.reflect.Field f = reader.getClass().getDeclaredField("encrypted");
                f.setAccessible(true);
                f.set(reader, false);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            retValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return retValue;
    }
}
