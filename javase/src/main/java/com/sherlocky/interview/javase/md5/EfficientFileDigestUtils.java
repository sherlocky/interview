package com.sherlocky.interview.javase.md5;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * need  commons-codec-1.6+.jar
 *
 * @author Sherlocky
 * @date 2017-10-23
 */
public class EfficientFileDigestUtils {
    static MessageDigest MD5 = null;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        }
    }

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
        for (File f : fs) {
            long t1 = System.currentTimeMillis();
            String md5 = EfficientFileDigestUtils.getFileMD5(f);
            long t2 = System.currentTimeMillis();
            System.out.println(md5 + " : " + f.getName() + " : " + (t2 - t1));
        }
    }

    public static String getFileMD5(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        return getFileMD5(new File(filePath));
    }

    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     */
    public static String getFileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }

            return new String(Hex.encodeHex(MD5.digest()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 求一个字符串的md5值
     *
     * @param target 字符串
     * @return md5 value
     */
    public static String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }
}
