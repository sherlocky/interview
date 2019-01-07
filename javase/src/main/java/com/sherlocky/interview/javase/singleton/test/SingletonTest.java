package com.sherlocky.interview.javase.singleton.test;

import com.sherlocky.interview.javase.singleton.*;

/**
 * @author zhangcx
 * @date 2018-08-23
 */
public class SingletonTest {

    public static void main(String[] args) {
        int times = 5;
        Thread[] threads = new Thread[times]; 
        for (int i = 0; i < times; i++) {
            threads[i] = new TestThread();
        }
        for (int i = 0; i < times; i++) {
            threads[i].start();
        }
    }
}

class TestThread extends Thread {
    @Override
    public void run() {
        System.out.println("Singleton: " + LazySingleton.getInstance().hashCode()
                + "   InnerClassSingleton: " + InnerClassSingleton.getInstance().hashCode()
                + "   StaticCodeBlockSingleton: " + StaticCodeBlockSingleton.getInstance().hashCode()
                + "   EnumSingleton: " + EnumSingletonClazz.getInstance().hashCode()
                + "   CASSingleton: " + CASSingleton.getInstance().hashCode()
        );
    }
}