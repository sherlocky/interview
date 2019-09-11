package com.sherlocky.interview.algorithm.consistenthash;

import com.google.common.util.concurrent.AtomicLongMap;
import com.sherlocky.interview.algorithm.consistenthash.util.MockIpUtil;
import com.sherlocky.interview.algorithm.consistenthash.util.StatisticsUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 一致性哈希算法 测试
 *
 * @author: zhangcx
 * @date: 2019/9/10 20:07
 */
public class ConsistentHashNodeLocatorTest {
    private String[] ips = MockIpUtil.ips();

    /**
     * 测试分布的离散情况
     */
    @Test
    public void testDistribution() {
        List<MemcachedNode> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new MemcachedNode(new InetSocketAddress(ip, 8080)));
        }
        HashNodeLocator hashNodeLocator = new CommonConsistentHashNodeLocator(servers, DefaultHashAlgorithm.KETAMA_HASH);
        // 构造 50000 随机请求
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            keys.add(UUID.randomUUID().toString());
        }
        // 统计分布
        AtomicLongMap<MemcachedNode> atomicLongMap = AtomicLongMap.create();
        for (MemcachedNode server : servers) {
            atomicLongMap.put(server, 0);
        }
        for (String key : keys) {
            MemcachedNode node = hashNodeLocator.getPrimary(key);
            atomicLongMap.getAndIncrement(node);
        }
        System.out.println(StatisticsUtil.variance(atomicLongMap.asMap().values().toArray(new Long[]{})));
        System.out.println(StatisticsUtil.standardDeviation(atomicLongMap.asMap().values().toArray(new Long[]{})));

    }

    /**
     * 测试节点新增删除后的变化程度
     */
    @Test
    public void testNodeAddAndRemove() {
        List<MemcachedNode> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new MemcachedNode(new InetSocketAddress(ip, 8080)));
        }
        /* 随机下线10个服务器, 先shuffle，然后选择0到90，简单模仿随机算出的目的 */
//        Collections.shuffle(servers);
        List<MemcachedNode> serverChanged = servers.subList(0, 90);
        HashNodeLocator loadBalance = new CommonConsistentHashNodeLocator(servers, DefaultHashAlgorithm.KETAMA_HASH);
        HashNodeLocator changedLoadBalance = new CommonConsistentHashNodeLocator(serverChanged, DefaultHashAlgorithm.KETAMA_HASH);

        // 构造 50000 随机请求
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            keys.add(UUID.randomUUID().toString());
        }
        int count = 0;
        for (String invocation : keys) {
            MemcachedNode origin = loadBalance.getPrimary(invocation);
            MemcachedNode changed = changedLoadBalance.getPrimary(invocation);
            if (!origin.getSocketAddress().equals(changed.getSocketAddress())) count++;
        }
        System.out.println(count / 50000D);
    }
}