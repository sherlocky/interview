package com.sherlocky.interview.javase.holder;
/**
 * 【核心作用】不通过返回值在一个方法中改变一个对象
 * <p>Java的参数传递只有传值，Java中的基本类型（PS：int、long、boolean等）存放在栈内存中，
 * Java对象引用（PS：Integer、Long、Boolean等其他Java对象）参数也存放在栈内存中，
 * 但是引用指向的具体的值却存放在堆内存中。</p>
 * @author: zhangcx
 * @date: 2019/10/24 14:31
 */
public class HolderTest {
    public static void main(String[] args) {
        int iA = 1;
        Integer iB = new Integer(11);
        System.out.println("初始状态--> " + "iA: " + iA + "    iB: " + iB);

        change(iA, iB);
        System.out.println("方法一    --> " + "iA: " + iA + "    iB: " + iB);

        change2(iA, iB);
        System.out.println("方法二    --> " + "iA: " + iA + "    iB: " + iB);

        /**
         * javax.xml.ws.Holder 原生提供了 Holder 泛型类
         */
        Holder<Integer> holder = new Holder<Integer>(iB);
        change3(iA, holder);
        System.out.println("方法三    --> " + "iA: " + iA + "    iB: " + holder.value);
        System.out.println("|-> 只有使用了 Holder 的方法真正改变了值");
    }

    public static void change(int aa, Integer bb) {
        aa = 2;
        bb = 22;
    }

    public static void change2(int aa, Integer bb) {
        aa = new Integer(2);
        bb = new Integer(22);
    }

    public static void change3(int aa, Holder<Integer> bb) {
        aa = 2;
        bb.value = new Integer(22);
    }
}