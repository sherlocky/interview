package com.sherlocky.interview.multithread.semaphore;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * Semaphore 是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信
 * 号，做完自己的申请后归还，超过阈值后，线程申请许可信号将会被阻塞。
 * Semaphore 可以用来 构建一些对象池，资源池之类的，比如数据库连接池。
 *
 * Semaphore 其实和锁有点类似，它一般用于控制对某组资源的访问权限。
 *
 * <p>Semaphore 基本能完成 ReentrantLock 的所有工作，使用方法也与之类似，通过 acquire()与
 * release()方法来获得和释放临界资源。经实测， Semaphone.acquire()方法默认为可响应中断锁，
 * 与 ReentrantLock.lockInterruptibly()作用效果一致，也就是说在等待临界资源的过程中可以被
 * Thread.interrupt()方法中断。</p>
 *
 * <p>此外， Semaphore 也实现了可轮询的锁请求与定时锁的功能，除了方法名 tryAcquire 与 tryLock
 * 不同，其使用方法与 ReentrantLock 几乎一致。 Semaphore 也提供了公平与非公平锁的机制，也
 * 可在构造函数中进行设定。</p>
 *
 * <p>Semaphore 的锁释放操作也由手动进行，因此与 ReentrantLock 一样，为避免线程因抛出异常而
 * 无法正常释放锁的情况发生，释放锁的操作也必须在 finally 代码块中完成。</p>
 *
 * <p>可以创建计数为 1 的 Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态</p>
 *
 * @author: zhangcx
 * @date: 2019/5/28 13:50
 */
public class SemaphoreSample {

    public static void main(String[] args) {
        /**
         * 假设一个工厂有 5 台机器，但是有 8 个工人，一台机器同时只能被一个工人使用
         * ，只有使用完了，其他工人才能继续使用。
         */
        int num = 8; // 工人数
        // 创建一个计数阈值为 5 的信号量对象
        Semaphore semaphore = new Semaphore(5); // 机器数
        IntStream.range(0, num).forEach((idx) -> {
            new Worker(idx, semaphore).start();
        });
    }

    static class Worker extends Thread {
        private int no;
        private Semaphore semaphore;

        public Worker(int no, Semaphore semaphore) {
            this.no = no;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                // 申请许可
                semaphore.acquire();
                System.out.println("工人" + this.no + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.no + "释放出机器");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放许可
                semaphore.release();
            }
        }
    }
}
