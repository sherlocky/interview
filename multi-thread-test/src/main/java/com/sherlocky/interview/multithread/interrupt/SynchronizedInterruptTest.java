package com.sherlocky.interview.multithread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 中断与synchronized
 * @author: zhangcx
 * @date: 2018/11/26 15:01
 */
public class SynchronizedInterruptTest {
    /**
     * <b>注意非阻塞状态调用interrupt()并不会导致中断状态重置。</b>
     *
     * 综合所述，可以简单总结一下中断两种情况：
     * - 1.一种是当线程处于阻塞状态或者试图执行一个阻塞操作时，我们可以使用实例方法interrupt()进行线程中断，执行中断操作后将会抛出interruptException异常(该异常必须捕捉无法向外抛出)并将中断状态复位，
     * - 2.另外一种是当线程处于运行状态时，我们也可调用实例方法interrupt()进行线程中断，但同时必须【手动判断中断状态】，并编写中断线程的代码(其实就是结束run方法体的代码)。
     * 有时我们在编码时可能需要兼顾以上两种情况，那么就可以如下编写：
     */
    public void run() {
        try {
            //判断当前线程是否已中断,注意interrupted方法是静态的,执行后会对中断状态进行复位
            while (!Thread.interrupted()) {
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (InterruptedException e) {

        }
    }
}

/**
 * 事实上线程的中断操作对于正在等待获取的锁对象的synchronized方法或者代码块并不起作用，
 * 也就是对于synchronized来说，如果一个线程在等待锁，那么结果只有两种，
 * - 1.要么它获得这把锁继续执行
 * - 2.要么它就保存等待，即使调用中断线程的方法，也不会生效。
 */
class SynchronizedBlocked implements Runnable{

    public synchronized void f() {
        System.out.println("Trying to call f()");
        while (true) {// Never releases lock
            Thread.yield();
            // 和sleep()一样不会释放（放弃）资源锁
            // 表示 线程让步。它能让当前线程由“运行状态”进入到“就绪状态”,从而让其它具有相同优先级的等待线程获取执行权；
            // 但是，并不能保证在当前线程调用yield()之后，其它具有相同优先级的线程就一定能获得执行权；也有可能是当前线程又进入到“运行状态”继续运行
            // 顾名思义，就是说当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，
            // 让自己或者其它的线程运行，注意是让自己或者其他线程运行，并不是单纯的让给其他线程
        }
    }

    /**
     * 在构造器中创建新线程并启动获取对象锁
     */
    public SynchronizedBlocked() {
        //该线程已持有当前实例锁
        new Thread() {
            @Override
            public void run() {
                f(); // Lock acquired by this thread
            }
        }.start();
    }

    @Override
    public void run() {
        //中断判断
        while (true) {
            if (Thread.interrupted()) {
                System.out.println("中断线程!!");
                break;
            } else {
                f();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedBlocked sync = new SynchronizedBlocked();
        System.out.println("1");
        Thread t = new Thread(sync);
        System.out.println("2");
        //启动后调用f()方法,无法获取当前实例锁处于等待状态
        t.start();
        System.out.println("3");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("4");
        //中断线程,无法生效
        t.interrupt();
        System.out.println("5");
    }

    /**
     * 我们在SynchronizedBlocked构造函数中创建一个新线程并启动获取调用f()获取到当前实例锁，由于SynchronizedBlocked自身也是线程，启动后在其run方法中也调用了f()，但由于对象锁被其他线程占用，导致t线程只能等到锁，此时我们调用了t.interrupt();但并不能中断线程。
     */
}
