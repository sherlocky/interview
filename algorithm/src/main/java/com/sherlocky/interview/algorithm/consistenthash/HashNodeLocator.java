package com.sherlocky.interview.algorithm.consistenthash;

/**
 * 实现 Memcached  分布式缓存场景下的一致性哈希算法
 * <p>
 * NodeLocator 是分布式缓存场景下一致性哈希算法的抽象，
 * 它有一个 getPrimary 函数，接收一个缓存数据的 key 值，输出存储该缓存数据的服务器（节点）实例。
 * </p>
 * @author: zhangcx
 * @date: 2019/9/10 18:58
 */
public interface HashNodeLocator {
    /**
     * Get the primary location for the given key.
     *
     * @param k the object key
     * @return the QueueAttachment containing the primary storage for a key
     */
    MemcachedNode getPrimary(String k);
}