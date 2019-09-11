package com.sherlocky.interview.algorithm.consistenthash;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 实现 Memcached  分布式缓存场景下的一致性哈希算法
 * <p>
 * NodeLocator 是分布式缓存场景下一致性哈希算法的抽象，
 * 它有一个 getPrimary 函数，接收一个缓存数据的 key 值，输出存储该缓存数据的服务器（节点）实例。
 * </p>
 *
 * @author: zhangcx
 * @date: 2019/9/10 19:34
 */
public abstract class AbstractConsistentHashNodeLocator implements HashNodeLocator {
    /**
     * 默认加入 12 个虚拟节点
     */
    protected final static int VIRTUAL_NODE_SIZE = 12;
    /**
     * 虚拟节点后缀
     */
    protected final static String VIRTUAL_NODE_SUFFIX = "-";
    /**
     * 哈希环
     */
    protected volatile TreeMap<Long, MemcachedNode> hashRing;
    /**
     * 虚拟节点个数
     */
    protected final int virtualNodeSize;
    /**
     * 哈希算法
     */
    protected final HashAlgorithm hashAlgorithm;

    /**
     * @param memcachedNodes 缓存服务器节点
     * @param hashAlg 使用的哈希算法
     */
    protected AbstractConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlg) {
        this(memcachedNodes, hashAlg, VIRTUAL_NODE_SIZE);
    }

    /**
     * @param memcachedNodes 缓存服务器节点
     * @param hashAlg 使用的哈希算法
     * @param virtualNodeSize 虚拟节点个数
     */
    protected AbstractConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlg, int virtualNodeSize) {
        this.hashAlgorithm = hashAlg;
        // 先赋值哈希算法，再构建哈希环
        this.hashRing = buildConsistentHashRing(memcachedNodes);
        this.virtualNodeSize = virtualNodeSize;
    }

    /**
     * 构建hash环
     * @param servers
     * @return
     */
    abstract TreeMap<Long, MemcachedNode> buildConsistentHashRing(List<MemcachedNode> servers);

    /**
     * 获取给定 key 的缓存节点存储位置
     *
     * @param key
     * @return
     */
    @Override
    public MemcachedNode getPrimary(String key) {
        // 首先使用 HashAlgorithm 计算出 key 值对应的哈希值
        long hash = hashAlgorithm.hash(key);
        // 然后调用 getNodeForKey 函数从 TreeMap 中获取对应的最近的缓存服务器节点实例
        return getHashNode(hashRing, hash);
    }

    /**
     * 根据hash值找到环上服务器节点
     * @param hashRing
     * @param hash
     * @return
     */
    protected MemcachedNode getHashNode(TreeMap<Long, MemcachedNode> hashRing, long hash) {
        /* 向右找到第一个key */
        Map.Entry<Long, MemcachedNode> locatedNode = hashRing.ceilingEntry(hash);
        /* 想象成为一个环，超出尾部取出第一个 */
        if (locatedNode == null) {
            locatedNode = hashRing.firstEntry();
        }
        return locatedNode.getValue();
    }
}
