package com.sherlocky.interview.algorithm.consistenthash;

import java.util.List;
import java.util.TreeMap;

/**
 * Ketama算法的一致性哈希实现
 *
 * <p>
 * 一致性哈希算法的不同语言有不同的实现方式，其中较为有名的一种实现叫Ketama算法，
 * 一些开源框架譬如spymemcached，twemproxy等都内置了该算法的实现。
 * </p>
 *
 * <p>借鉴了 kiritomoe 博文中的实现和 spymemcached 客户端代码</p>
 *
 * @author: zhangcx
 * @date: 2019/9/10 18:58
 */
public class KetamaConsistentHashNodeLocator extends AbstractConsistentHashNodeLocator {
    protected KetamaConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlgorithm) {
        super(memcachedNodes, hashAlgorithm);
    }

    protected KetamaConsistentHashNodeLocator(List<MemcachedNode> memcachedNodes, HashAlgorithm hashAlgorithm, int virtualNodeSize) {
        super(memcachedNodes, hashAlgorithm, virtualNodeSize);
    }

    /**
     * 构建hash环
     *
     * @param servers
     * @return
     */
    @Override
    protected TreeMap<Long, MemcachedNode> buildConsistentHashRing(List<MemcachedNode> servers) {
        TreeMap<Long, MemcachedNode> virtualNodeRing = new TreeMap<>();
        for (MemcachedNode server : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE / 4; i++) {
                byte[] digest = KeyUtil.computeMd5(server.getSocketAddress().toString() + VIRTUAL_NODE_SUFFIX + i);
                for (int h = 0; h < 4; h++) {
                    Long k = ((long) (digest[3 + h * 4] & 0xFF) << 24)
                            | ((long) (digest[2 + h * 4] & 0xFF) << 16)
                            | ((long) (digest[1 + h * 4] & 0xFF) << 8)
                            | (digest[h * 4] & 0xFF);
                    virtualNodeRing.put(k, server);
                }
            }
        }
        return virtualNodeRing;
    }
}
