package com.sherlocky.interview.algorithm.consistenthash;

import java.util.List;
import java.util.TreeMap;

/**
 * 通用的一致性哈希算法的实现，使用 TreeMap 作为一致性哈希环的数据结构。
 * <p>
 *     参考：<a href="https://mp.weixin.qq.com/s?__biz=Mzg2NjE5NDQyOA==&mid=2247483762&idx=1&sn=f377cf428ac99d9c940d7e4c485de42e">分布式数据缓存中的一致性哈希算法</a>
 * </p>
 * @author: zhangcx
 * @date: 2019/9/10 18:58
 */
public class CommonConsistentHashNodeLocator extends AbstractConsistentHashNodeLocator {
    public CommonConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlgorithm) {
        super(memcachedNodes, hashAlgorithm);
    }

    protected CommonConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlgorithm, int virtualNodeSize) {
        super(memcachedNodes, hashAlgorithm, virtualNodeSize);
    }

    /**
     * 构建hash环
     * @param nodes
     * @return
     */
    @Override
    protected TreeMap<Long, MemcachedNode> buildConsistentHashRing(List<MemcachedNode> nodes) {
        TreeMap<Long, MemcachedNode> virtualNodeRing = new TreeMap<>();
        for (MemcachedNode node : nodes) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                // 新增虚拟节点的方式如果有影响，也可以抽象出一个由物理节点扩展虚拟节点的类
                virtualNodeRing.put(hashAlgorithm.hash(node.getSocketAddress().toString() + VIRTUAL_NODE_SUFFIX + i), node);
            }
        }
        return virtualNodeRing;
    }
}