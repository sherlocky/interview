package com.sherlocky.interview.javase.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Arrays.asList() 将数组转换为集合后,底层其实还是数组，是一个泛型方法。
 * <p>体现的是适配器模式，只是转换接口，后台的数据依然是数组。</p>
 *
 * @author: zhangcx
 * @date: 2019/7/2 19:41
 */
public class ArraysAsListSample {

    public static void main(String[] args) {
        /**
         * Arrays.asList() 底层还是数组，对原数组的修改，会影响返回的list
         */
        String[] myArray = {"Apple", "Banana", "Orange"};
        List<String> myList = Arrays.asList(myArray);
        //上面两个语句等价于下面一条语句
        List<String> myList2 = Arrays.asList("Apple", "Banana", "Orange");
        System.out.println(myList);
        System.out.println(myList2);
        myArray[0] = "Peach";
        System.out.println(myList);


        /**
         * 当传入一个原生数据类型数组时，Arrays.asList() 的真正得到的参数
         * 就不是数组中的元素，而是数组对象本身！
         * 此时List 只有一个元素，唯一元素就是这个数组。
         */
        // Arrays.asList()是泛型方法，传递的数组必须是对象数组，而不是基本类型。
        int[] intArray = {1, 2, 3};
        List intList = Arrays.asList(intArray);
        // 1
        System.out.println(intList.size());
        // 数组地址值
        System.out.println(intList.get(0));
        // 报错：ArrayIndexOutOfBoundsException
        // System.out.println(intList.get(1));
        int[] array = (int[]) intList.get(0);
        System.out.println(array[0]);

        /**
         * 应该使用包装类型数组
         */
        Integer[] integerArray = {1, 2, 3};
        List integerList = Arrays.asList(integerArray);
        System.out.println(integerList);

        /**
         * 注意：
         * Arrays.asList() 方法返回的并不是 {@Link java.util.ArrayList}，
         * 而是 {@link java.util.Arrays} 的一个内部类 {@link java.util.Arrays$ArrayList},
         * 这个内部类并没有实现集合的修改方法。
         */
        // 运行时报错：UnsupportedOperationException
        // integerList.add(4);
        // 运行时报错：UnsupportedOperationException
        // integerList.remove(1);
        // 运行时报错：UnsupportedOperationException
        // integerList.clear();


        /**
         * 正确的将数组转换为ArrayList
         */
        // 1.new ArrayList<> （推荐）
        List trueList = new ArrayList<>(Arrays.asList("a", "b", "c"));
        trueList.add("ddd");
        System.out.println(trueList);

        // 2.使用 Java8 的Stream（推荐）
        List trueIntegerList = Arrays.stream(integerArray).collect(Collectors.toList());
        // 基本类型也可以实现转换（依赖boxed的装箱操作）
        List trueIntList = Arrays.stream(intArray).boxed().collect(Collectors.toList());

        // 另外还可以使用 Guava 的 Lists.newArrayList()
        // 或者，使用 Apache Commons Collections 的 CollectionUtils.addAll()
    }
}
