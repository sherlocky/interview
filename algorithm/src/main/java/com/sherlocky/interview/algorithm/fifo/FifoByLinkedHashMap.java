package com.sherlocky.interview.algorithm.fifo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 【先进先出】
 * 使用LinkedHashMap实现FIFO
 * @author: zhangcx
 * @date: 2019/5/15 16:59
 */
public class FifoByLinkedHashMap<K, V> {

    /** 最大缓存大小 */
    private final int MAX_CACHE_SIZE;
    /** 默认加载因子 */
    private final float DEFAULT_LOAD_FACTORY = 0.75f;

    LinkedHashMap<K, V> map;

    public FifoByLinkedHashMap(int cacheSize) {
        this.MAX_CACHE_SIZE = cacheSize;
        // 容量，将初始化大小设置为(缓存大小 / loadFactor) + 1，这样就可以在元素数目达到缓存大小时，也不会进行扩容了
        // 【HashMap中：size是键值对大小，容量（默认16）是 capacity，加载因子是loadFactory（默认0.75）,超过capacity*loadFactory阈值，则会自动扩容一倍】
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) + 1;

        /**
         * 第三个参数accessOrder设置为true，代表linkedlist按访问顺序排序，可作为LRU缓存
         * 第三个参数accessOrder设置为false，代表按插入顺序排序，可作为FIFO缓存（默认即为false）
         */
        map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, false) {
            /**
             * 通过重写这个方法来控制缓存元素的删除
             * <p>当返回true的时候，就会remove其中最早插入的元素。</p>
             * <p>当缓存满了后，就可以通过返回true删除最早插入的元素，达到FIFO的要求</p>
             * @param eldest
             * @return
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void remove(K key) {
        map.remove(key);
    }

    public synchronized Set<Map.Entry<K, V>> getAll() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            stringBuilder.append(String.format("%s: %s  ", entry.getKey(), entry.getValue()));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        FifoByLinkedHashMap<Integer, Integer> fifo = new FifoByLinkedHashMap(5);
        fifo.put(1, 1);
        fifo.put(2, 2);
        fifo.put(3, 3);
        /* 1: 1  2: 2  3: 3 */
        System.out.println(fifo);
        fifo.get(1);
        /* 1: 1  2: 2  3: 3   */
        System.out.println(fifo);
        fifo.put(4, 4);
        fifo.put(5, 5);
        fifo.put(6, 6);
        /* 2: 2  3: 3  4: 4  5: 5  6: 6 */
        System.out.println(fifo);
    }
}
