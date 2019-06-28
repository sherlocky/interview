package com.sherlocky.interview.javase.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * HashMap 遍历
 *
 * <p>HashMap：允许 K 和 V 为 null</p>
 * <p>ConcurrentHashMap：不允许 K 和 V 为 null</p>
 *
 * @author: zhangcx
 * @date: 2019/6/28 13:31
 */
public class MapForeachSample {

    public static void main(String[] args) {
        // 菱形泛型，diamond 方式，即<>
        Map<String, Integer> data = new HashMap<>(16);
        /**
         * 集合初始化时，指定集合初始值大小，HashMap 使用 HashMap(int initialCapacity) 初始化。
         *
         * initialCapacity = (需要存储的元素个数 / 负载因子 loader factor) + 1
         * 负载因子 loader factor 默认为：0.75
         *
         * 如果暂时无法确定初始值大小，请设置为 16（即默认值）。
         */
        // 全省略方式
        Map<String, String> data2 = new HashMap(16);

        IntStream.range(0, 6).forEach(i -> {
            data.put(String.valueOf(i), i);
        });

        /**
         * 遍历Map
         */
        // 1.keySet 方式（不推荐） -- 其实是遍历了 2 次
        for (String key : data.keySet()) {
            System.out.println(key + " : " + data.get(key));
        }
        // 2.entrySet 方式（推荐） -- 之遍历一次就取出K和V，效率更高
        for (Map.Entry entry : data.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        // 3.Map.forEach （JDK8+下 推荐）
        data.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });

        // 【参考】 HashMap 在容量不够进行 resize 时由于高并发可能出现死链，导致 CPU 飙升，
        // 在开发过程中可以使用其它数据结构或加锁来规避此风险。
    }
}
