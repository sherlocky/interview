package com.sherlocky.interview.algorithm.consistenthash.benchmark;

import com.sherlocky.interview.algorithm.consistenthash.CommonConsistentHashNodeLocator;
import com.sherlocky.interview.algorithm.consistenthash.DefaultHashAlgorithm;
import com.sherlocky.interview.algorithm.consistenthash.HashNodeLocator;
import com.sherlocky.interview.algorithm.consistenthash.MemcachedNode;
import com.sherlocky.interview.algorithm.consistenthash.util.MockIpUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JMH只适合细粒度的方法测试，并不适用于系统之间的链路测试！
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class JMHBenchmarkTest {
    private String[] ips = MockIpUtil.ips();
    private HashNodeLocator nodeLocator;
    private List<String> keys;

    @Benchmark
    public void test() {
        for (String key : keys) {
            MemcachedNode node = nodeLocator.getPrimary(key);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHBenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void prepare() {
        List<MemcachedNode> servers = new ArrayList<>();
        for (String ip : ips) {
            servers.add(new MemcachedNode(new InetSocketAddress(ip, 8080)));
        }
        nodeLocator = new CommonConsistentHashNodeLocator(servers, DefaultHashAlgorithm.KETAMA_HASH);
        // 构造 50000 随机请求
        keys = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            keys.add(UUID.randomUUID().toString());
        }

    }

    @TearDown
    public void shutdown() {
    }
}