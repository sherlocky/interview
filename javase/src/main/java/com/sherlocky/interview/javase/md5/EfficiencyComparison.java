package com.sherlocky.interview.javase.md5;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * FileDigestUtils 和 EfficientFileDigestUtils 效率比较
 *
 * @author zhangcx
 * @date 2017-10-23
 */
public class EfficiencyComparison {

    public static void main(String[] args) {
        File fo = new File("G:/tmp");
        File[] fs = fo.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile() && f.length() > 0 && f.getName().indexOf(".") > -1) {
                    return true;
                }
                return false;
            }
        });

        System.out.println(StringUtils.leftPad("文件名 | ", 50) + "文件大小 |  "
                + StringUtils.leftPad("FileDigestUtils用时", 18) + " | EfficientFileDigestUtils用时");
        System.out.println("__________________________________________________________________________________________________");
        for (File f : fs) {
            String md5 = null;
            long t1 = System.currentTimeMillis();
            // 1.FileDigestUtils
            try {
                md5 = FileDigestUtils.fileMD5(f.getAbsolutePath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            long t2 = System.currentTimeMillis();
            // 2.EfficientFileDigestUtils
            String fileMd5Key = EfficientFileDigestUtils.getFileMD5(f);
            long t3 = System.currentTimeMillis();

            String fileNameNoExt = StringUtils.substringBeforeLast(f.getName(), ".");
            System.out.println(StringUtils.leftPad(f.getName(), 50) + ":"
                    + StringUtils.leftPad(FileUtils.byteCountToDisplaySize(f.length()), 8) + " | "
                    + StringUtils.leftPad(String.valueOf(t2 - t1) + "ms", 20) + " | "
                    + StringUtils.leftPad(String.valueOf(t3 - t2) + "ms", 8));
        }
    }
}
