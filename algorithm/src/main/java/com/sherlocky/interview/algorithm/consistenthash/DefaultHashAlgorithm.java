package com.sherlocky.interview.algorithm.consistenthash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.CRC32;

/**
 * 默认的哈希算法
 * <p>提供常用的哈希算法</p>
 *
 * @author: zhangcx
 * @date: 2019/9/10 19:05
 */
public enum DefaultHashAlgorithm implements HashAlgorithm {
    /**
     * java 本地哈希方法 (String.hashCode()).
     */
    NATIVE_HASH,
    /**
     * CRC_HASH as used by the perl API. This will be more consistent both
     * across multiple API users as well as java versions, but is mostly likely
     * significantly slower.
     */
    CRC_HASH,
    /**
     * FNV hashes are designed to be fast while maintaining a low collision rate.
     * The FNV speed allows one to quickly hash lots of data while maintaining a
     * reasonable collision rate.
     *
     * @see <a href="http://www.isthe.com/chongo/tech/comp/fnv/">fnv
     * comparisons</a>
     * @see <a href="http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash">fnv at
     * wikipedia</a>
     */
    FNV1_64_HASH,
    /**
     * Variation of FNV.
     */
    FNV1A_64_HASH,
    /**
     * 32-bit FNV1.
     */
    FNV1_32_HASH,
    /**
     * 32-bit FNV1a.
     */
    FNV1A_32_HASH,
    /**
     * MD5-based hash algorithm used by ketama.
     */
    KETAMA_HASH,
    /**
     * MurmurHash算法（高运算性能，低碰撞率，hadoop、memcached等使用）
     */
    MURMUR_HASH;

    private static final long FNV_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV_64_PRIME = 0x100000001b3L;

    private static final long FNV_32_INIT = 2166136261L;
    private static final long FNV_32_PRIME = 16777619;

    /**
     * 计算给定 key 的 hash 值
     *
     * @param key
     * @return 一个正整数 hash 值
     */
    @Override
    public long hash(String key) {
        long rv = 0;
        int len = key.length();
        switch (this) {
            case NATIVE_HASH:
                rv = key.hashCode();
                break;
            case CRC_HASH:
                // return (crc32(shift) >> 16) & 0x7fff;
                CRC32 crc32 = new CRC32();
                crc32.update(KeyUtil.getKeyBytes(key));
                rv = (crc32.getValue() >> 16) & 0x7fff;
                break;
            case FNV1_64_HASH:
                // Thanks to pierre@demartines.com for the pointer
                rv = FNV_64_INIT;
                for (int i = 0; i < len; i++) {
                    rv *= FNV_64_PRIME;
                    rv ^= key.charAt(i);
                }
                break;
            case FNV1A_64_HASH:
                rv = FNV_64_INIT;
                for (int i = 0; i < len; i++) {
                    rv ^= key.charAt(i);
                    rv *= FNV_64_PRIME;
                }
                break;
            case FNV1_32_HASH:
                rv = FNV_32_INIT;
                for (int i = 0; i < len; i++) {
                    rv *= FNV_32_PRIME;
                    rv ^= key.charAt(i);
                }
                break;
            case FNV1A_32_HASH:
                rv = FNV_32_INIT;
                for (int i = 0; i < len; i++) {
                    rv ^= key.charAt(i);
                    rv *= FNV_32_PRIME;
                }
                break;
            case KETAMA_HASH:
                byte[] bKey = KeyUtil.computeMd5(key);
                rv = ((long) (bKey[3] & 0xFF) << 24)
                        | ((long) (bKey[2] & 0xFF) << 16)
                        | ((long) (bKey[1] & 0xFF) << 8)
                        | (bKey[0] & 0xFF);
                break;
            case MURMUR_HASH:
                rv = murmurHash(key);
                break;
            default:
                assert false;
        }
        /** Truncate to 32-bits */
        return rv & 0xffffffffL;
    }

    private long murmurHash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(KeyUtil.getKeyBytes(key));
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }
}
