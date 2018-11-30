package com.sherlocky.interview.javase.interview.algorithm.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Assert;
import org.junit.Test;

/**
 * 简单测试下Guava实现的布隆过滤器
 * <p>内存利用率高，老年代的使用率很低，没有出现 fullGC</p>
 * @author: zhangcx
 * @date: 2018/11/30 9:15
 */
public class GuavaBloomFilterTest {

    @Test
    public void guavaTest() {
        long star = System.currentTimeMillis();
        // expectedInsertions: 预计的数量
        // fpp: 误报率
        /**
         * Guava 会通过你预计的数量以及误报率帮你计算出你应当会使用的
         * 数组大小 numBits 以及需要计算几次 Hash 函数 numHashFunctions
         */
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 10000000, 0.01);
        for (int i = 0; i < 10000000; i++) {
            filter.put(i);
        }
        Assert.assertTrue(filter.mightContain(1));
        Assert.assertTrue(filter.mightContain(2));
        Assert.assertTrue(filter.mightContain(3));
        Assert.assertFalse(filter.mightContain(10000000));
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - star));
    }
}
