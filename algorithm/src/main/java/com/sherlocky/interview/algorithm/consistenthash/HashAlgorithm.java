package com.sherlocky.interview.algorithm.consistenthash;

/**
 * 对哈希算法的抽象
 * <p>
 *     一致性哈希算法可以使用各种普通的哈希算法，比如说 CRC ，MurmurHash 和 FNV 等，参考： {@link DefaultHashAlgorithm}。
 * </p>
 * @author: zhangcx
 * @date: 2019/9/10 18:58
 */
public interface HashAlgorithm {
    /**
     * Compute the hash for the given key.
     *
     * @return a positive integer hash
     */
    long hash(final String k);
}