package com.sherlocky.interview.multithread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 推荐使用 guava 提供的 ThreadFactoryBuilder 来创建线程池
 *
 * @author: zhangcx
 * @date: 2020/8/31 9:54
 * @since:
 */
public class GuavaExecutorTest {
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 通过上述方式创建线程时，不仅可以避免 OOM 的问题，还可以自定义线程名称，
     * 更加方便的出错的时候溯源
     *
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            pool.execute(new Thread());
        }
    }
}
