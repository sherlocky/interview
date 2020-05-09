package com.sherlocky.interview.javase.guava;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 编程中有时需要统计代码的的执行耗时
 * Guava Stopwatch 计时工具类，借助他统计程序执行时间，使用方式非常灵活。
 * <p>
 *     commons-lang3 与 Spring-core 也有这个工具类，使用方式大同小异
 * </p>
 * @author: zhangcx
 * @date: 2020/5/9 10:22
 * @since:
 */
public class GuavaStopwatchTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建之后立刻计时，若想主动开始计时
        Stopwatch stopwatch = Stopwatch.createStarted();
        // 创建计时器，但是需要主动调用 start 方法开始计时
        // Stopwatch stopwatch = Stopwatch.createUnstarted();
        // stopWatch.start();
        // 模拟其他代码耗时
        TimeUnit.SECONDS.sleep(2L);

        // 当前已经消耗的时间
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));

        TimeUnit.SECONDS.sleep(2L);

        // 停止计时 未开始的计时器调用 stop 将会抛错 IllegalStateException
        stopwatch.stop();
        // 再次统计总耗时
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));

        // 重新开始，将会在原来时间基础计算，若想重新从 0开始计算，需要调用 stopwatch.reset()
        stopwatch.start();
        TimeUnit.SECONDS.sleep(2L);
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));
    }
}
