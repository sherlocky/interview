package com.sherlocky.interview.multithread.volatiledemo;

/**
 * <p>volatile修饰的变量如果是对象或数组之类的，其含义是对象获数组的地址具有可见性，
 * 但是数组或对象内部的成员改变不具备可见性。</p>
 *
 * 注意：
 * <ul>
 * <li>1）用 volatile 修饰数组和对象不是不可以，要注意一点：修改操作要从 volatile 变量逐级引用，去找到要修改的变量，保证修改是刷新到主存中的值对应的变量；
 *      读取操作，也要以 volatile 变量为根，逐级去定位，这样保证修改即使刷新到主存中 volatile 变量指向的堆内存，
 *      读取能够每次从主存的 volatile 变量指向的堆内存去读，保证数据的一致性。</li>
 * <li>2）在保证了 1）的前提下，因为大家读取修改的都是同一块内存，所以变相的符合 happen-before 规则中的程序顺序规则，具有 happen-before 性。</li>
 * </ul>
 *
 * https://gblog.sherlocky.com/volatile/
 *
 */
public class VolatileObjectTestAgain implements Runnable {
    /**
     * 此时加上volatile也无法结束 while 循环了
     * <p>给{@link ObjectASub}的 flag 加上 volatile 才能结束 while 循环</p>
     */
    private volatile ObjectA a;

    public VolatileObjectTestAgain(ObjectA a) {
        this.a = a;
    }

    public ObjectA getA() {
        return a;
    }

    public void setA(ObjectA a) {
        this.a = a;
    }

    @Override
    public void run() {
        long i = 0;
        ObjectASub sub = a.getSub();
        while (!sub.isFlag()) {
            i++;
        }
        System.out.println("stop My Thread " + i);
    }

    public static void main(String[] args) throws InterruptedException {
        // 如果启动的时候加上-server 参数则会 输出 Java HotSpot(TM) Server VM
        System.out.println(System.getProperty("java.vm.name"));
        ObjectASub sub = new ObjectASub();
        ObjectA sa = new ObjectA();
        sa.setSub(sub);
        VolatileObjectTestAgain test = new VolatileObjectTestAgain(sa);
        new Thread(test).start();

        Thread.sleep(1000);
        sub.setFlag(true);
        Thread.sleep(1000);
        System.out.println("Main Thread " + sub.isFlag());
    }

    static class ObjectA {
        private ObjectASub sub;

        public ObjectASub getSub() {
            return sub;
        }

        public void setSub(ObjectASub sub) {
            this.sub = sub;
        }
    }

    static class ObjectASub {
        /**
         * 此处加上 volatile 才能结束 while 循环
         */
        private boolean flag;
        // private volatile boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}