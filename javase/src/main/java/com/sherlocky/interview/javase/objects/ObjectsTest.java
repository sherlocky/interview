package com.sherlocky.interview.javase.objects;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 在JDK7版本的时候，Java引入了java.util.Objects工具类。
 * <p>封装了一些平时使用频度很高或容易出错的操作</p>
 *
 * 其构造方法为私有方法，实现为抛出一个 AssertionError
 *
 * @author: zhangcx
 * @date: 2019/9/12 10:27
 */
public class ObjectsTest {

    @Test
    public void testEquals() {
        String s1 = null;
        String s2 = "";
        String s3 = null;
        String s4 = "";
        /**
         * Objects.equals()用于比较两个对象的引用是否相同
         * 有别于object.equals()，这个方法可以避免空指针异常
         */
        System.out.println(Objects.equals(s1, s2)); // false
        System.out.println(Objects.equals(s1, s3)); // true
        System.out.println(Objects.equals(s3, s1)); // true
        System.out.println(Objects.equals(s2, s4)); // true
    }

    @Test
    public void testDeepEquals() {
        /**
         * deepEquals() 扩展成了可以支持数组
         */
        String[] a1 = {"", "", ""};
        String[] a2 = {"", "", ""};
        // false
        System.out.println(Objects.equals(a1, a2));
        // true
        System.out.println(Objects.deepEquals(a1, a2));
    }

    @Test
    public void testHashCode() {
        /**
         * 和Object.hashCode()类似，只是在对象为null时返回的散列值为0而已
         */
        // 0
        System.out.println(Objects.hashCode(null));
        System.out.println(Objects.hashCode("java"));
        /** 生成对象的散列值，包括数组 */
        System.out.println(Objects.hash("hello", "world", "java"));
    }

    @Test
    public void testToString() {
        // null
        System.out.println(Objects.toString(null));
        System.out.println(Objects.toString(null, "xxx"));
        int i = 125;
        // 125
        System.out.println(Objects.toString(i));
    }

    @Test
    public void testCompare() {
        String s1 = null;
        String s2 = "";
        String s3 = null;
        String s4 = "x";
        Comparator<String> comparator = (x, y) -> {
            if (x == null && y == null) {
                return 0;
            }
            if (x == null) {
                return -1;
            }
            if (y == null) {
                return 1;
            }
            return x.compareTo(y);
        };
        System.out.println(Objects.compare(s1, s2, comparator)); // -1
        System.out.println(Objects.compare(s2, s1, comparator)); // 1
        System.out.println(Objects.compare(s1, s3, comparator)); // 0
        System.out.println(Objects.compare(s3, s1, comparator)); // 0
        System.out.println(Objects.compare(s2, s4, comparator)); // -1
        System.out.println(Objects.compare(s4, s2, comparator)); // 1
    }

    @Test(expected = NullPointerException.class)
    public void testRequireNonNull() {
        String str = null;
        System.out.println("将会抛出空指针异常~");
        // Objects.requireNonNull(str);
        Objects.requireNonNull(str, "字符串 str 不能为空~");

    }

    @Test
    public void testIsNullNonNull() {
        List<String> strList = new ArrayList<>();
        strList.add("1");
        strList.add("b");
        strList.add("");
        strList.add(null);
        strList.add("@");
        strList.add(null);
        strList.add("。");
        /**
         * 这两个方法用于判断对象为null和对象不为null。
         * 通常情况下，我们不会直接使用这两个方法，而是使用比较操作符==和!=。
         *
         * 这两个方法主要用在jdk8开始支持的流计算里面。
         */
        System.out.println(String.format("%d 个为空~", strList.stream().filter(Objects::isNull).count()));
        System.out.println("____________nonNUll_________");
        strList.stream().filter(Objects::nonNull).forEach(System.out::println);
    }
}
