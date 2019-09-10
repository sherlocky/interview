package com.sherlocky.interview.algorithm.lru;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * LRU是Least Recently Used的缩写，即最近最久未使用
 * <p>【LRU缓存的思想】</p>
 * <ul>
 * <li>固定缓存大小，需要给缓存分配一个固定的大小。</li>
 * <li>每次读取缓存都会改变缓存的使用时间，将缓存的存在时间重新刷新。</li>
 * <li>需要在缓存满了后，将最近最久未使用的缓存删除，再添加最新的缓存。</li>
 * </ul>
 *
 * 基于LinkedHashMap来实现LRU算法，LinkedHashMap是为自动扩容的，
 * 当table数组中元素大于Capacity * loadFactor的时候，就会自动进行两倍扩容。
 * 但是为了使缓存大小固定，就需要在初始化的时候传入容量大小和负载因子。
 *
 * @author: zhangcx
 * @date: 2019/5/15 9:54
 */
public class LruByLinkedHashMap<K, V> {

    /** 最大缓存大小 */
    private final int MAX_CACHE_SIZE;
    /** 默认加载因子 */
    private final float DEFAULT_LOAD_FACTORY = 0.75f;

    LinkedHashMap<K, V> map;

    public LruByLinkedHashMap(int cacheSize) {
        this.MAX_CACHE_SIZE = cacheSize;
        // 容量，将初始化大小设置为(缓存大小 / loadFactor) + 1，这样就可以在元素数目达到缓存大小时，也不会进行扩容了
        // 【HashMap中：size是键值对大小，容量（默认16）是 capacity，加载因子是loadFactory（默认0.75）,超过capacity*loadFactory阈值，则会自动扩容一倍】
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) + 1;

        /**
         * 第三个参数accessOrder设置为true，代表linkedlist按访问顺序排序，可作为LRU缓存
         * 第三个参数accessOrder设置为false，代表按插入顺序排序，可作为FIFO缓存
         */
        map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, true) {
            /**
             * 通过重写这个方法来控制缓存元素的删除
             * <p>当返回true的时候，就会remove其中最久的元素。</p>
             * <p>当缓存满了后，就可以通过返回true删除最久未被使用的元素，达到LRU的要求</p>
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
        LruByLinkedHashMap<Integer, Integer> lru = new LruByLinkedHashMap(5);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);
        /* 1: 1  2: 2  3: 3 */
        System.out.println(lru);
        lru.get(1);
        /* 2: 2  3: 3  1: 1 */
        System.out.println(lru);
        lru.put(4, 4);
        lru.put(5, 5);
        lru.put(6, 6);
        /* 3: 3  1: 1  4: 4  5: 5  6: 6 */
        System.out.println(lru);
    }
}
