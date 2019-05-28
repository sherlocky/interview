package com.sherlocky.interview.multithread.lock.service;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>ReentrantLock 与 synchronized</p>
 * <ul>
 * <li>1. ReentrantLock 通过方法 lock()与 unlock()来进行加锁与解锁操作，与 synchronized 会
 * 被 JVM 自动解锁机制不同， ReentrantLock 加锁后需要手动进行解锁。为了避免程序出
 * 现异常而无法正常解锁的情况，使用 ReentrantLock 必须在 finally 控制块中进行解锁操
 * 作。</li>
 * <li>2. ReentrantLock 相比 synchronized 的优势是可中断、公平锁、多个锁。这种情况下需要
 * 使用 ReentrantLock</li>
 * </ul>
 *
 * <p>Condition 类和 Object 类锁方法区别区别</p>
 * <ul>
 * <li>1. Condition 类的 awiat 方法和 Object 类的 wait 方法等效</li>
 * <li>2. Condition 类的 signal 方法和 Object 类的 notify 方法等效</li>
 * <li>3. Condition 类的 signalAll 方法和 Object 类的 notifyAll 方法等效</li>
 * <li>4. ReentrantLock 类可以唤醒指定条件的线程，而 object 的唤醒是随机的</li>
 *</ul>
 *
 * <p>tryLock 和 lock 和 lockInterruptibly 的区别</p>
 * <ul>
 * <li>1. tryLock 能获得锁就返回 true，不能就立即返回 false， tryLock(long timeout,TimeUnit unit)，可以增加时间限制，如果超过该时间段还没获得锁，返回 false</li>
 * <li>2. lock 能获得锁就返回 true，不能的话一直等待获得锁</li>
 * <li>3. lock 和 lockInterruptibly，如果两个线程分别执行这两个方法，但此时中断这两个线程，lock 不会抛出异常，而 lockInterruptibly 会抛出异常</li>
 * </ul>
 *
 * @author sherlock
 */
public class MyService {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean hasValue = false;

    public void set() {
        try {
            lock.lock();
            while (hasValue == true) {
                System.out.println("有可能★★连续");
                /**
                 * 通过创建 Condition 对象来使线程 wait，调用 lock.await 之前，需要先调用 lock.lock 获得锁（同步监视器），
                 * 否则会报 IllegalMonitorStateException
                 */
                condition.await();
            }
            System.out.println("打印★");
            hasValue = true;
            // signal方法可以唤醒wait线程
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void get() {
        try {
            lock.lock();
            while (hasValue == false) {
                System.out.println("有可能☆☆连续");
                condition.await();
            }
            System.out.println("打印☆");
            hasValue = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
