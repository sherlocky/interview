package com.sherlocky.interview.multithread.futuretask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.*;

/**
 * 使用 FutureTask 来演练烧水泡茶经典程序
 *
 * <p>洗水壶 1 分钟 -> 烧开水 15 分钟 --></p>
 * <p>洗茶壶 1 分钟 -> 洗茶杯 1 分钟 -> 拿茶叶 2 分钟 --></p>
 *
 * @author: zhangcx
 * @date: 2020/8/1 13:28
 * @since:
 */
public class MakeTeaSample {
    private static final Log log = LogFactory.getLog(MakeTeaSample.class); 
    /**
     * 如果串行总共需要 20 分钟，但很显然在烧开水期间，我们可以洗茶壶/洗茶杯/拿茶叶
     * 这样总共需要 16 分钟，可以节约4分钟时间
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * ThreadPoolExecutor是JDK并发包提供的一个线程池服务
         *
         * corePoolSize：         核心线程数，会一直存活，即使没有任务，线程池也会维护线程的最少数量
         * maximumPoolSize： 线程池维护线程的最大数量
         * keepAliveTime：      线程池维护线程所允许的空闲时间，当线程空闲时间达到keepAliveTime，该线程会退出，直到线程数量等于corePoolSize。
         *      如果allowCoreThreadTimeout设置为true，则所有线程均会退出直到线程数量为0。
         * unit： 线程池维护线程所允许的空闲时间的单位、可选参数值为：TimeUnit中的几个静态属性：NANOSECONDS、MICROSECONDS、MILLISECONDS、SECONDS。
         * workQueue： 线程池所使用的缓冲队列，常用的是：java.util.concurrent.ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue
         * handler： 线程池中的数量大于maximumPoolSize，对拒绝任务的处理策略，默认值ThreadPoolExecutor.AbortPolicy()。
         */
        ExecutorService es = new ThreadPoolExecutor(
                2,
                2,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        // ExecutorService es2 = Executors.newFixedThreadPool(2);
        // 创建线程2的FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        // 创建线程1的FutureTask
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        es.submit(ft1);
        es.submit(ft2);

        log.info(ft1.get());

        es.shutdown();

        log.info("开始品茶吧~");
    }

    static class T1Task implements Callable<String> {
        /**
         * 线程1希望在水烧开的那一刹那就可以拿到茶叶直接泡茶
         * 在线程 1 的FutureTask 中获取 线程 2 FutureTask 的返回结果就可以了
         */
        private FutureTask<String> ft2;
        public T1Task(FutureTask<String> ft2) {
            this.ft2 = ft2;
        }

        @Override
        public String call() throws Exception {
            log.info("T1.1:洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            log.info("T1.2:烧开水...");
            TimeUnit.SECONDS.sleep(15);


            String ft2Result = ft2.get();
            log.info(String.format("T1 拿到了 %s， 开始泡茶", ft2Result));
            return "T1:上茶！";
        }
    }

    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("T2.1:洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            log.info("T2.2:洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            log.info("T2.3:拿茶叶...");
            TimeUnit.SECONDS.sleep(1);
            return "T2:准备好的金骏眉~";
        }
    }

}
