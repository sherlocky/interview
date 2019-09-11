package com.sherlocky.interview.algorithm.consistenthash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: zhangcx
 * @date: 2019/9/10 19:11
 */
public class KeyUtil {
    private static MessageDigest md5Digest = null;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    private KeyUtil() {

    }

    /**
     * Get the bytes for a key.
     *
     * @param key the key
     * @return the bytes
     */
    public static byte[] getKeyBytes(String key) {
        try {
            return key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the keys in byte form for all of the string keys.
     *
     * @param keys a collection of keys
     * @return return a collection of the byte representations of keys
     */
    public static Collection<byte[]> getKeyBytes(Collection<String> keys) {
        Collection<byte[]> rv = new ArrayList<byte[]>(keys.size());
        for (String s : keys) {
            rv.add(getKeyBytes(s));
        }
        return rv;
    }

    /**
     * 计算给定key的md5值
     */
    public static byte[] computeMd5(String key) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(KeyUtil.getKeyBytes(key));
        return md5.digest();
    }
}
