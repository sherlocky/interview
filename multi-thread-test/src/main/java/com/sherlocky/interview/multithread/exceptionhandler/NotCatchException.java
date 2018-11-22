package com.sherlocky.interview.multithread.exceptionhandler;

import com.sherlocky.interview.multithread.exceptionhandler.extthread.MyThread;

// 不处理异常
public class NotCatchException {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
    }
}
