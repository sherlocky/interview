package com.sherlocky.interview.multithread.volatiledemo;

/**
 * volatile：
 * <ul>
 * <li>1.可见性：对一个 volatile 变量的读，总是能看到（任意线程）对这个 volatile 变量最后的写入。</li>
 * <li>2.原子性：对任意单个 volatile 变量的**读/写**具有原子性(long, double 这 2 个 8 字节的除外)，但类似于 volatile++ 这种**复合**操作**不**具有原子性。</li>
 * <li>3.volatile 修饰的变量如果是对象或数组之类的，其含义是对象获数组的**地址**具有可见性，但是数组或对象内部的成员改变不具备可见性。</li>
 * </ul>
 * @author: zhangcx
 * @date: 2019/6/20 9:39
 */
public class VolatileObjectTest implements Runnable {
    /**
     * 加上 volatile 就可以正常结束While循环了
     * 这并不代表 volatile 变量修饰对象或者数组可以实现不同线程间可见性。
     * {@link VolatileObjectTestAgain}
     */
    private ObjectA a;
    // private volatile ObjectA a;

    public VolatileObjectTest(ObjectA a) {
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
        while (a.isFlag()) {
            i++;
//            System.out.println("------------------");
        }
        System.out.println("stop My Thread " + i);
    }

    public void stop() {
        a.setFlag(false);
    }

    public static void main(String[] args) throws InterruptedException {
        // 如果启动的时候加上-server 参数则会 输出 Java HotSpot(TM) Server VM
        System.out.println(System.getProperty("java.vm.name"));

        VolatileObjectTest test = new VolatileObjectTest(new ObjectA());
        new Thread(test).start();

        Thread.sleep(1000);
        test.stop();
        Thread.sleep(1000);
        System.out.println("Main Thread " + test.getA().isFlag());
    }

    static class ObjectA {
        private boolean flag = true;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

    }
}