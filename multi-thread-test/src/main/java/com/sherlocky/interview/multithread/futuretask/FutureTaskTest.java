package com.sherlocky.interview.multithread.futuretask;

import java.util.concurrent.*;

/**
 * 简单测试 FutureTask
 *
 * <p>FutureTask 就是一个实现 Future 模式，支持取消的异步处理器。</p>
 * 可参考<a href="https://mp.weixin.qq.com/s?__biz=MjM5NzMyMjAwMA==&mid=2651482143&idx=1&sn=483f860f37018218ef86b6895181a5a8&chksm=bd2504608a528d76f379242b3adc1af441d06aa2f77853c5ff5b81cc488ef8e2c0ca1524e470&mpshare=1&scene=1&srcid=#rd">FutureTask 在线程池中应用和源码解析</a>；<a href="www.jianshu.com/p/1fac6476e85f">FutureTask 在线程池中应用和源码解析</a>；<a href="https://blog.csdn.net/sx1119183530/article/details/79735348">多线程中Future与FutureTask的区别和联系</a>
 *
 * @author zhangcx
 * @date 2018-11-26
 */
public class FutureTaskTest {
    /**
     * 第一种方式： 使用【Callable+Future】获取执行结果
     */
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        // 创建Callable对象任务
        CallableDemo callableDemo = new CallableDemo();
        // 提交任务并获取执行结果
        Future<Integer> future = executor.submit(callableDemo);
        //关闭线程池
        executor.shutdown();

        try {
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis() + " : 主线程在执行其他任务");

            if (future.get() != null) {
                //输出获取到的结果
                System.out.println("future.get() --> " + future.get());
            } else {
                //输出获取到的结果
                System.out.println("future.get() 未获取到结果");
            }
            System.out.println(System.currentTimeMillis() + " : 所有任务都执行完了。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二种方式： 使用【Callable+FutureTask】获取执行结果
     */
    public static void main2(String[] args) {
        // 创建线程池
        ExecutorService es = Executors.newSingleThreadExecutor();
        // 创建Callable对象任务
        CallableDemo callableDemo2 = new CallableDemo();
        // 创建FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(callableDemo2);
        // 执行任务
        es.submit(futureTask);
        // 关闭线程池
        es.shutdown();

        try {
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis() + "主线程在执行其他任务");

            if (futureTask.get() != null) {
                //输出获取到的结果
                System.out.println("futureTask.get() --> " + futureTask.get());
            } else {
                //输出获取到的结果
                System.out.println("futureTask.get() 未获取到结果");
            }
            System.out.println(System.currentTimeMillis() + " : 所有任务都执行完了。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class CallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(System.currentTimeMillis() + " : Callable子线程开始计算啦！");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 5000; i++) {
            sum += i;
        }
        System.out.println(System.currentTimeMillis() + " : Callable子线程计算结束！");
        return sum;
    }
}

class Task2 extends FutureTask<Integer> {

    public Task2(Callable<Integer> callable) {
        super(callable);
    }

    public Task2(Runnable runnable, Integer result) {
        super(runnable, result);
    }

    @Override
    public void run() {
        System.out.println("子线程在进行计算");
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}