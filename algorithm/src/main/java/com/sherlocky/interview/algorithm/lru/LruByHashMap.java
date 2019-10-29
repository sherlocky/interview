package com.sherlocky.interview.algorithm.lru;

import java.util.HashMap;

/**
 * 使用散列表和双向链表实现缓存
 * <p>
 *     LRU 是 Least Recently Used 的缩写，这种算法认为最近使用的数据是热门数据，下一次很大概率将会再次被使用。
 *     而最近很少被使用的数据，很大概率下一次不再用到。当缓存容量满的时候，优先淘汰最近很少使用的数据。
 * </p>
 * <p>可参考：<a href="https://mp.weixin.qq.com/s?__biz=MzU3NzczMTAzMg==&mid=2247485912&idx=1&sn=0b560c36109b5b6ea69ac08938ac68ca">散列表+双向链表实现LRU</a></p>
 * <p>
 *     具体步骤：
 *     <ul>
 *         <li>1.新数据直接插入到列表头部</li>
 *         <li>2.缓存数据被命中，将数据移动到列表头部</li>
 *         <li>3.缓存已满的时候，移除列表尾部数据</li>
 *     </ul>
 * </p>
 * @author
 */
public class LruByHashMap<K, V> {
    private final int MAX_CACHE_SIZE;
    private Entry<K, V> head;
    private Entry<K, V> tail;

    private HashMap<K, Entry<K, V>> cache;

    public LruByHashMap(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        cache = new HashMap<>();
    }

    /**
     * 将节点加入到头结点，如果容量已满，将会删除尾结点
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            // 如果缓存列表已满，则移除列表尾部数据
            if (cache.size() >= MAX_CACHE_SIZE) {
                cache.remove(tail.key);
                removeTail();
            }
            entry = new Entry<>();
            entry.key = key;
            entry.value = value;
            // 加入节点，并移动到头部（加入头结点）
            moveToHead(entry);
            cache.put(key, entry);
        } else {
            // 移动到头部
            entry.value = value;
            moveToHead(entry);
        }
    }

    /**
     * 如果节点不存在，返回 null；
     * 如果存在，将节点移动到头结点，并返回节点的数据
     * @param key
     * @return
     */
    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        // 存在移动节点
        moveToHead(entry);
        return entry.value;
    }

    public void remove(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            if (entry == head) {
                Entry<K, V> next = head.next;
                head.next = null;
                head = next;
                head.pre = null;
            } else if (entry == tail) {
                Entry<K, V> prev = tail.pre;
                tail.pre = null;
                tail = prev;
                tail.next = null;
            } else {
                entry.pre.next = entry.next;
                entry.next.pre = entry.pre;
            }
            cache.remove(key);
        }
    }

    private void removeTail() {
        if (tail != null) {
            Entry<K, V> prev = tail.pre;
            if (prev == null) {
                head = null;
                tail = null;
            } else {
                tail.pre = null;
                tail = prev;
                tail.next = null;
            }
        }
    }

    private void moveToHead(Entry<K, V> entry) {
        if (entry == head) {
            return;
        }
        if (entry.pre != null) {
            entry.pre.next = entry.next;
        }
        if (entry.next != null) {
            entry.next.pre = entry.pre;
        }
        if (entry == tail) {
            Entry<K, V> prev = entry.pre;
            if (prev != null) {
                tail.pre = null;
                tail = prev;
                tail.next = null;
            }
        }

        if (head == null || tail == null) {
            head = tail = entry;
            return;
        }

        entry.next = head;
        head.pre = entry;
        entry.pre = null;
        head = entry;
    }

    private Entry<K, V> getEntry(K key) {
        return cache.get(key);
    }

    private static class Entry<K, V> {
        Entry<K, V> pre;
        Entry<K, V> next;
        K key;
        V value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Entry<K, V> entry = head;
        while (entry != null) {
            stringBuilder.append(String.format("%s:%s ", entry.key, entry.value));
            entry = entry.next;
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        LruByHashMap<Integer, Integer> lru2 = new LruByHashMap<>(5);
        lru2.put(1, 1);
        /* 1:1 */
        System.out.println(lru2);
        lru2.put(2, 2);
        /* 2:2 1:1 */
        System.out.println(lru2);
        lru2.put(3, 3);
        /* 3:3 2:2 1:1 */
        System.out.println(lru2);
        lru2.get(1);
        /* 1:1 3:3 2:2 */
        System.out.println(lru2);
        lru2.put(4, 4);
        lru2.put(5, 5);
        lru2.put(6, 6);
        /* 6:6 5:5 4:4 1:1 3:3 */
        System.out.println(lru2);
    }
}