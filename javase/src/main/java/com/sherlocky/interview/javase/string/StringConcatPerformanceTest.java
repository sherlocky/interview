package com.sherlocky.interview.javase.string;

import org.junit.Test;

/**
 * 测试字符串拼接性能
 * <p>
 * 为什么阿里巴巴不建议在for循环中使用"+"进行字符串拼接（非循环体可以使用，做了优化，本质是StringBuilder）
 * https://mp.weixin.qq.com/s?__biz=MzI3NzE0NjcwMg==&mid=2650123039&idx=1&sn=a3aa2348f7ed0c9d5014ba66737285b4
 * <p>
 *  Java 8中字符串拼接新姿势：StringJoiner
 *  https://mp.weixin.qq.com/s/AXZLjBvrfyNZd0OUeo29Mg
 *
 * @author
 */
public class StringConcatPerformanceTest {
    private String s1 = "我";
    private String s2 = "张飞";
    private Integer i1 = 12;
    private String s4 = "打篮球";
    private String S5 = "警察";
    private int count = 100000;

    @Test
    public void testPlus() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String s = s1 + "叫" + s2 + "，今年" + i1 + "岁了，我喜欢" + s4 + "，我的梦想是当一名" + S5 + "。";
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void testFormat() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String s = String.format("%s叫%s，今年%s岁了，我喜欢%s，我的梦想是当一名%s。", s1, s2, i1, s4, S5);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void testStringBuilder() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String s = new StringBuilder().append(s1).append("叫").append(s2)
                    .append("，今年").append(i1).append("岁了，我喜欢")
                    .append(s4).append("，我的梦想是当一名").append(S5).append("。").toString();
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}