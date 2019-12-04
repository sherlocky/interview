package com.sherlocky.interview.multithread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.LongStream;

/**
 * ForkJoin 性能比较
 * @author: zhangcx
 * @date: 2019/12/4 11:23
 * @since:
 */
public class ForkJoinDemoTest {
    /**
     * 总数 90亿
     */
    private static Long count = 9_000_000_000L;

    public static void main(String[] args) {
        long sum = 0;
        // 1.forkjoin 性能
        /**
         * task要通过ForkJoinPool来执行，分割的子任务也会添加到当前工作线程的双端队列中，
         * 进入队列的头部。当一个工作线程中没有任务时，会从其他工作线程的队列尾部获取一个任务(工作窃取)。
         */
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool= new ForkJoinPool();
        ForkJoinDemo fj = new ForkJoinDemo(0L, count);
        sum = forkJoinPool.invoke(fj);
        long end = System.currentTimeMillis();
        // 本地测试大约：2272ms
        System.out.println("ForkJion计算，总和是： " + sum + " 用时:" + (end - start) + "ms");

        // 2.普通方法
        start = System.currentTimeMillis();
        sum = 0;
        for (long i = 0; i <= count; i++) {
            sum += i;
        }
        end = System.currentTimeMillis();
        // 本地测试大约：3287ms
        System.out.println("普通for循环计算，总和是： " + sum + " 用时:" + (end - start) + "ms");

        // 3.Stream 流 并行+sum
        start = System.currentTimeMillis();
        sum = LongStream.rangeClosed(0, count).parallel().sum();
        end = System.currentTimeMillis();
        // 本地测试大约：1850ms
        System.out.println("Stream 流 并行+sum，总和是： " + sum + " 用时:" + (end - start) + "ms");

        // 4.Stream 流 并行+规约方法
        start = System.currentTimeMillis();
        sum = LongStream.rangeClosed(0, count).parallel().reduce(0, Long::sum);
        end = System.currentTimeMillis();
        // 本地测试大约：2254ms
        System.out.println("Stream 流 并行+规约计算，总和是： " + sum + " 用时:" + (end - start) + "ms");
    }
}
