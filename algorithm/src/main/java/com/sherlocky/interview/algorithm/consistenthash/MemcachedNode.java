package com.sherlocky.interview.algorithm.consistenthash;

import java.net.SocketAddress;

/**
 * Memcached  分布式缓存服务器（节点）
 * @author: zhangcx
 * @date: 2019/9/10 18:58
 */
public class MemcachedNode {
    private final SocketAddress socketAddress;

    public MemcachedNode(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    /**
     * Get the SocketAddress of the server to which this node is connected.
     */
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}