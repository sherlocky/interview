package com.sherlocky.interview.multithread.threadlocal;

import lombok.Data;

import java.util.stream.IntStream;

/**
 * <p>【参考】 ThreadLocal 对象使用 static 修饰，ThreadLocal 无法解决共享对象的更新问题。
 *
 * 说明：这个变量是针对一个线程内所有操作共享的，所以设置为静态变量，所有此类实例共享此静态变
 * 量，也就是说在类第一次被使用时装载，只分配一块存储空间，所有此类的对象(只要是这个线程内定义
 * 的)都可以操控这个变量。</p>
 *
 * <p>必须回收自定义的 ThreadLocal 变量，尤其在线程池场景下，线程经常会被复用，
 * 如果不清理自定义的 ThreadLocal 变量，可能会影响后续业务逻辑和造成内存泄露等问题。
 * 尽量在代理中使用 try-finally 块进行回收。</p>
 *
 * <p>可参考：https://gblog.sherlocky.com/threadlocal/</p>
 *
 * @author: zhangcx
 * @date: 2019/6/28 14:23
 */
public class ThreadLocalSample {
    // private static 修饰
    private static ThreadLocal<Session> sessionHandle = ThreadLocal.<Session>withInitial(Session::new);

    @Data
    public static class Session {
        private String id;
        private String user;
        private String status;
    }

    public static void main(String[] args) {
        IntStream.range(0, 5).forEach((i) -> {
            new Thread(() -> {
                Session session = sessionHandle.get();
                try {
                    session.setId(String.valueOf(i));;
                    System.out.println(session.getId());
                    session.setStatus("close");
                    System.out.println(session.getStatus());
                    // ...
                } finally {
                    // 使用完必须回收自定义的 ThreadLocal 变量
                    sessionHandle.remove();
                }
            }).start();
        });
    }
}
