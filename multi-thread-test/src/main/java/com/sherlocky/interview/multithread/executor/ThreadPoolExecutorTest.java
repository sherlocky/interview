package com.sherlocky.interview.multithread.executor;

import java.util.concurrent.*;

/**
 * JAVA线程池详细理解可参考：https://www.cnblogs.com/dolphin0520/p/3932921.html
 *
 * <p>corePoolSize :线程池的核心池大小，在创建线程池之后，线程池默认没有任何线程。
 * 当有任务过来的时候才会去创建创建线程执行任务。换个说法，线程池创建之后，线程池中的线程数为0，
 * 当任务过来就会创建一个线程去执行，直到线程数达到corePoolSize 之后，就会被到达的任务放在队列中（注意是到达的任务）。
 * 换句更精炼的话：corePoolSize 表示允许线程池中允许同时运行的最大线程数。
 * <p>
 * 如果执行了线程池的prestartAllCoreThreads()方法，线程池会提前创建并启动所有核心线程。</p>
 *
 * <p>maximumPoolSize :线程池允许的最大线程数，他表示最大能创建多少个线程。maximumPoolSize肯定是大于等于corePoolSize。</p>
 *
 * <p>Java doc中并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：</p>
 * <ul>
 * <li>Executors.newCachedThreadPool();        //创建一个缓冲池（corePoolSize设置为0，将maximumPoolSize设置为Integer.MAX_VALUE，
 * 使用的SynchronousQueue，也就是说来了任务就创建线程运行，当线程空闲超过60秒，就销毁线程）</li>
 * <li>Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池（corePoolSize和maximumPoolSize都设置为1，使用的LinkedBlockingQueue）</li>
 * <li>Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池（corePoolSize和maximumPoolSize值是相等的，使用的LinkedBlockingQueue）</li>
 * </ul>
 *
 * <p>而Ali Java开发手册中强制要求线程池不允许使用 Executors 去创建，而是要通过 ThreadPoolExecutor 的方式。</p>
 * <p>这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。</p>
 * <p>Executors 返回的线程池对象的弊端如下：</p>
 * <ul>
 *     <li>1） FixedThreadPool 和 SingleThreadPool：允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。</li>
 *     <li>2） CachedThreadPool：允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 * </ul>
 *
 * <p><strong>任务缓存队列及排队策略</strong></p>
 * 任务缓存队列，即workQueue，它用来存放等待执行的任务。类型为BlockingQueue<Runnable>，通常可以取下面三种类型：
 * <ul>
 * <li>1）ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；</li>
 * <li>2）LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；</li>
 * <li>3）synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。</li>
 * </ul>
 *
 * <p><strong>任务拒绝策略</strong></p>
 * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略：
 * <ul>
 * <li>ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。</li>
 * <li>ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。</li>
 * <li>ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）</li>
 * <li>ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务</li>
 * </ul>
 *
 * <p>线程池的关闭</p>
 * ThreadPoolExecutor提供了两个方法，用于线程池的关闭，分别是shutdown()和shutdownNow()，其中：
 * <ul>
 * <li>shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务</li>
 * <li>shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务</li>
 * </ul>
 *
 * @author: zhangcx
 * @date: 2019/5/16 11:07
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        int num = 15;
        num = 16; // 会抛出异常 java.util.concurrent.RejectedExecutionException，且程序不会终止。。

        for (int i = 0; i < num; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行完别的任务数目：" + executor.getCompletedTaskCount());
        }

        System.out.println("Shutdown 线程池。");
        executor.shutdown();
    }
}

class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task " + taskNum);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task " + taskNum + "执行完毕");
    }
}