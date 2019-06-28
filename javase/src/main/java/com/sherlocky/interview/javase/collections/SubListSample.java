package com.sherlocky.interview.javase.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * 在 Ali 的 Java 开发手册中明确要求谨慎使用 ArrayList 中的 subList() 方法
 *
 * <p>ArrayList 的 subList 结果不可强转成 ArrayList，否则会抛出 ClassCastException 异常，
 *      即 java.util.RandomAccessSubList cannot be cast to java.util.ArrayList。</p>
 *
 * <p>说明：subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList 而是 ArrayList 的一个视图</p>
 * <ul>
 *  <li>对父、子List做的非结构性修改（non-structural changes），都会影响到彼此。</li>
 *  <li>对子List做结构性修改，操作同样会反映到父List上。</li>
 *  <li>对父List做结构性修改，会导致子列表抛出异常ConcurrentModificationException。</li>
 * </ul>
 *
 * @author: zhangcx
 * @date: 2019/6/28 8:38
 */
public class SubListSample {
    public static void main(String[] args) {
        /**
         * subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList 而是 ArrayList 的一个视图。
         * 他们之间并没有继承关系，不能强转成 ArrayList！
         * SubList 这个类中单独定义了 set、get、size、add、remove 等方法。
         */
        List<String> sourceList = new ArrayList<String>() {{
            add("H");
            add("O");
            add("L");
            add("L");
            add("I");
            add("S");
        }};
        System.out.println("sourceList ：" + sourceList);
        List subList = sourceList.subList(2, 5);
        // ArrayList subList2 = sourceList.subList(2, 5); // 编译报错
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");

        /**
         * 对于 SubList 子列表的所有操作（包括结构/非结构性修改）最终会反映到原列表上
         */
        subList.set(1,"666");
        System.out.println("subList.set(1, \"666\")后：");
        System.out.println("sourceList ：" + sourceList);
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");
        subList.add("888");
        subList.add("999");
        System.out.println("subList.add(1, \"888\"), subList.add(1, \"999\") 后：");
        System.out.println("sourceList ：" + sourceList);
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");
        subList.remove("999");
        System.out.println("subList.remove(\"999\") 后：");
        System.out.println("sourceList ：" + sourceList);
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");

        /**
         * 对父List做的非结构性修改（non-structural changes），都会影响到子列表。
         */
        sourceList.set(2, "X");
        System.out.println("sourceList.set(0, \"X\") 后：");
        System.out.println("sourceList ：" + sourceList);
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");

        /**
         * 【高度注意】对父List做结构性修改，会导致子列表的访问、遍历、增加、删除
         * 抛出异常 ConcurrentModificationException。
         */
        sourceList.add("ZZZ");
        System.out.println("sourceList.add(\"ZZZ\") 后，访问 subList 会抛出异常。。：");
        System.out.println("sourceList ：" + sourceList);
        System.out.println("subList ：" + subList);
        System.out.println("---------------------------");
    }

}
