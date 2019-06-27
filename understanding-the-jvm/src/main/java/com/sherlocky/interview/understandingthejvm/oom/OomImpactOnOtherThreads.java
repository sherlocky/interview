package com.sherlocky.interview.understandingthejvm.oom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一个线程 OOM 对其他线程的影响
 * <p>一个线程OOM后，其他线程还能运行吗？</p>
 * <p>还能运行!</p>
 * <p>
 * OOM又分很多类型；
 * <ul>
 * <li>堆溢出（“java.lang.OutOfMemoryError: Java heap space”）</li>
 * <li>永久代溢出（“java.lang.OutOfMemoryError:Permgen space”）</li>
 * <li>不能创建线程（“java.lang.OutOfMemoryError:Unable to create new native thread”）</li>
 * </ul>
 *
 * <p>JVM启动参数设置为：</p>
 * <pre>-Xms16m -Xmx32m</pre>
 *
 * @author: zhangcx
 * @date: 2019/6/27 9:07
 */
public class OomImpactOnOtherThreads {
    public static void main(String[] args) {
        new Thread(() -> {
            List<byte[]> list = new ArrayList<byte[]>();
            while (true) {
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                byte[] b = new byte[1024 * 1024 * 1];
                list.add(b);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 线程二
        new Thread(() -> {
            while (true) {
                System.out.println(new Date().toString() + Thread.currentThread() + "==");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
/**
运行结果：
 Thu Jun 27 09:17:43 CST 2019Thread[Thread-0,5,main]==
 Thu Jun 27 09:17:43 CST 2019Thread[Thread-1,5,main]==
 Exception in thread "Thread-0" java.lang.OutOfMemoryError: Java heap space
 at com.sherlocky.interview.understandingthejvm.oom.OomImpactOnOtherThreads.lambda$main$0(OomImpactOnOtherThreads.java:31)
 at com.sherlocky.interview.understandingthejvm.oom.OomImpactOnOtherThreads$$Lambda$1/1023892928.run(Unknown Source)
 at java.lang.Thread.run(Thread.java:748)
 Thu Jun 27 09:17:44 CST 2019Thread[Thread-1,5,main]==
 Thu Jun 27 09:17:45 CST 2019Thread[Thread-1,5,main]==
 Thu Jun 27 09:17:46 CST 2019Thread[Thread-1,5,main]==
 Thu Jun 27 09:17:47 CST 2019Thread[Thread-1,5,main]==
 Thu Jun 27 09:17:48 CST 2019Thread[Thread-1,5,main]==
 Thu Jun 27 09:17:49 CST 2019Thread[Thread-1,5,main]==
 ......

 可以使用 jvisualvm 命令观察 JVM 堆空间的变化。

 当一个线程抛出OOM异常后，它所占据的内存资源会全部被释放掉，从而不会影响其他线程的运行！(栈溢出也是同理)

 总结：其实发生OOM的线程一般情况下会死亡，也就是会被终结掉，该线程持有的对象占用的heap都会被gc了，释放内存。
 因为发生OOM之前要进行gc，就算其他线程能够正常工作，也会因为频繁gc产生较大的影响。

 */