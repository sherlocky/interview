package com.sherlocky.interview.multithread.interrupt;

/**
 * interrupt() 方法并不能立即中断线程，该方法仅仅
 * 告诉线程外部已经有中断请求，至于是否中断还取决于线程自己
 */
public class InterrupTest implements Runnable {

    @Override
    public void run() {
        try {
            while (true) {
                /**
                 * 可以通过线程的 isInterrupted 方法进行检测
                 * 是否请求中断标志为true还是false
                 */
                Boolean a = Thread.currentThread().isInterrupted();
                System.out.println("in run() - about to sleep for 20 seconds-------" + a);
                Thread.sleep(20000);
                System.out.println("in run() - woke up");
            }
        } catch (InterruptedException e) {
            // false
            System.out.println(Thread.currentThread().isInterrupted());
            // false
            System.out.println(Thread.currentThread().isInterrupted());
            /**
             * 【重置 interrupted 状态】为 true
             *
             * 如果不加上这一句，那么c d将会都是false，
             * 因为在捕捉到InterruptedException异常的时候就会自动的中断标志置为了false。
             *
             * 如果在捕获了InterruptedException异常之后，你什么也不想做，那么至少就将标志重新置为true，
             * 以便栈中更高层的代码能知道中断，并且对中断作出响应。
             */
            Thread.currentThread().interrupt();
            // true
            System.out.println(Thread.currentThread().isInterrupted());
            // true
            System.out.println(Thread.currentThread().isInterrupted());
            /**
             * true
             * Thread.interrupted() 静态方法也可以检测标志位，
             * 但是静态方法它检测完以后会自动的将是否请求中断标志位置为：false。
             */
            Boolean c = Thread.interrupted();
            // false
            Boolean d = Thread.interrupted();
            System.out.println("c=" + c);
            System.out.println("d=" + d);
        }
    }

    public static void main(String[] args) {
        InterrupTest si = new InterrupTest();
        Thread t = new Thread(si);
        t.start();
        //主线程休眠2秒，从而确保刚才启动的线程有机会执行一段时间
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("in main() - interrupting other thread");
        //中断线程t
        t.interrupt();
        System.out.println("in main() - leaving");
    }
}