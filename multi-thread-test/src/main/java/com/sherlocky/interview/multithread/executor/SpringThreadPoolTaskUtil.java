package com.sherlocky.interview.multithread.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程任务池，需要需要新开线程来处理任务，则使用这个帮助类即可
 * <p>ThreadPoolTaskExecutor 是辅助 JDK 的 ThreadPoolExecutor 的工具类，
 * 它将属性通过 JavaBeans 的命名规则提供出来，方便进行配置</p>
 *
 * <p>ThreadPoolExecutor执行器的处理流程:</p>
 * <ul>
 * <li>(1)当线程池大小小于 corePoolSize 就新建线程，并处理请求.</li>
 * <li>(2)当线程池大小等于 corePoolSize，把请求放入 workQueue 中，池子里的空闲线程就去从 workQueue 中取任务并处理.</li>
 * <li>(3)当 workQueue 放不下新入的任务时，新建线程加入线程池，并处理请求，如果池子大小撑到了 maximumPoolSize 就用 RejectedExecutionHandler 来做拒绝处理.</li>
 * <li>(4)另外，当线程池的线程数大于 corePoolSize 的时候，多余的线程会等待 keepAliveTime 长的时间，如果无请求可处理就自行销毁.</li>
 * </ul>
 *
 * @author sherlock
 */
public class SpringThreadPoolTaskUtil {
    private static Log logger = LogFactory.getLog(SpringThreadPoolTaskUtil.class);

    public static ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    static {
        //如果池中的实际线程数小于1，无论是否其中有空闲的线程,都会给新的任务产生新的线程
        taskExecutor.setCorePoolSize(10);
        //如果提交的线程数大于corePoolSize并且小于maxPoolSize，只会创建corePoolSize的线程数被创建，然后将任务提交到队列中，直到队列慢为止
        taskExecutor.setMaxPoolSize(30);
        taskExecutor.initialize();
    }

    public static void addTask(Runnable task) {
        taskExecutor.execute(task);
    }

    public static void shutdown() {
        try {
            taskExecutor.shutdown();
        } catch (Exception ex) {
            logger.error("关闭ThreadPoolTaskUtil失败", ex);
        }
    }

    public static void main(String[] args) {
        SpringThreadPoolTaskUtil.addTask(null);
    }
}
