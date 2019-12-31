package com.sherlocky.interview.algorithm.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 缓存，降级和限流是大型分布式系统中的三把利剑。
 * 目前限流主要有【漏桶】和【令牌桶】两种算法。
 * <p>
 * Guava RateLimiter是一个谷歌提供的限流工具，RateLimiter基于令牌桶算法，
 * 可以有效限定单个JVM实例上某个接口或方法的流量，不能用作全局流量控制。
 *
 * @author: zhangcx
 * @date: 2019/12/16 10:28
 * @since:
 */
public class GuavaRateLimiterSample {
    public static void main(String[] args) {
        // QPS 设置为5，代表一秒钟只允许处理五个并发请求
        RateLimiter rateLimiter = RateLimiter.create(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // 测试工作任务 任务数 10
        int nTasks = 10;
        CountDownLatch countDownLatch = new CountDownLatch(nTasks);
        long start = System.currentTimeMillis();

        /**
         * 开启10个线程
         * 每个任务大概耗时1000毫秒
         *
         * 虽然有10个线程，但使用RateLimiter设置了qps为5，一秒内只允许五个并发请求被处理。
         */
        for (int i = 0; i < nTasks; i++) {
            final int j = i;
            executorService.submit(() -> {
                rateLimiter.acquire(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread().getName() + " gets job " + j + " done");
                countDownLatch.countDown();
            });
        }

        executorService.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("10 jobs gets done by 5 threads concurrently in " + (end - start) + " milliseconds");
    }
}
