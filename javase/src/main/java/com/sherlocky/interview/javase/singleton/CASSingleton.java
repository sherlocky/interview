package com.sherlocky.interview.javase.singleton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 更加优美的Singleton，使用CAS锁实现单例（线程安全）
 *
 * @author: zhangcx
 * @date: 2019/1/7 10:53
 */
public class CASSingleton {
    /**
     * 利用AtomicReference
     */
    private static final AtomicReference<CASSingleton> INSTANCE = new AtomicReference<CASSingleton>();

    /**
     * 私有化
     */
    private CASSingleton() {
    }

    /**
     * 用CAS确保线程安全
     */
    public static final CASSingleton getInstance() {
        for (; ; ) {
            CASSingleton current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new CASSingleton();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public static void main(String[] args) {
        CASSingleton singleton1 = CASSingleton.getInstance();
        CASSingleton singleton2 = CASSingleton.getInstance();
        System.out.println(singleton1 == singleton2);
    }
}
