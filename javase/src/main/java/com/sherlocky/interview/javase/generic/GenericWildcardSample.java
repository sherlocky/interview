package com.sherlocky.interview.javase.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>泛型通配符</p>
 *
 * <strong>PECS(Producer Extends Consumer Super)原则：</strong>
 * <ul>
 * <li>第1.频繁往外读取内容的[生产者（Producer）]，只读，适合用<? extends T>。</li>
 * <li>第2.经常往里插入内同的[消费者（Consumer）]，只写，适合用<? super T>。</li>
 * <li>第3.如果一个泛型集合即要生产，又要消费，则不能使用泛型通配符声明集合。</li>
 * </ul>
 * <ul>
 * <li>- 1.泛型通配符 <? extends T> 用来接收返回的数据，此写法的泛型集合不能使用 add 方法。</li>
 * <li>- 2.泛型通配符 <? super T> 用来写入数据，此写法的泛型集合不能使用 get 方法，作为接口调用赋值时易出错。</li>
 * </ul>
 *
 * PECS 原则的实现可以参考 {@link java.util.Collections#copy(List, List)}
 *
 * @author: zhangcx
 * @date: 2019/6/28 11:24
 */
public class GenericWildcardSample {
    public static void main(String[] args) {
        /**
         * <? extends Number> 表示包括T在内的任何T的子类。
         *
         * - 1.读取操作一定能从列表中读取到的元素的类型是 Number。
         * - 2.写入操作使用并不能往 List<? extends Number> 中插入任何类型的对象，
         *      因为不能保证列表实际指向的类型是什么，并不能保证列表中实际存储什么类型的对象。
         *
         * 以下赋值都是合法的：
         */
        // Number "extends" Number (in this context)
        List<? extends Number> foo1 = new ArrayList<Number>();
        // Integer extends Number
        List<? extends Number> foo2 = new ArrayList<Integer>();
        // Double extends Number
        List<? extends Number> foo3 = new ArrayList<Double>();

        /**
         *  <? extends Fruit> 表示Fruit是这个传入的泛型的基类（Fruit是泛型的上界）。
         *  这个集合内的元素都是 Fruit 的子类型，但是到底是哪个子类型不知道，
         *  所以添加哪个子类型，编译器都认为是危险的，所以直接禁止添加！
         */
        List<? extends Fruit> fruitList = new ArrayList<>();
        // 都编译报错
        // fruitList.add(new Fruit());
        // fruitList.add(new Apple());



        /**
         * <? super T> 表示包括T在内的任何T的父类。
         *
         * - 1.写入操作一定可以往列表中插入 Integer 类型元素，因为Integer、Number、Object都支持Integer。
         * - 2.读取操作不能保证 <? super Integer> 读取出的元素是Integer类型，
         *      因为不能保证列表实际指向的类型是什么，并不能保证列表中实际存储什么类型的对象。
         *
         * 以下赋值是合法的：
         */
        // Integer is a "superclass" of Integer (in this context)
        List<? super Integer> foo4 = new ArrayList<Integer>();
        // Number is a superclass of Integer
        List<? super Integer> foo5 = new ArrayList<Number>();
        // Object is a superclass of Integer
        List<? super Integer> foo6 = new ArrayList<Object>();

        /**
         * 在无泛型限制定义的集合赋值给泛型限制的集合时，在使用集合元素时，
         * 需要进行instanceof 判断，避免抛出 ClassCastException 异常。
         */
        List<String> generics = null;
        List notGenerics = new ArrayList(10);
        notGenerics.add(new Object());
        notGenerics.add(new Integer(1));
        generics = notGenerics;
        // 此处抛出 java.lang.ClassCastException 异常
        String string = generics.get(0);

        /**
         *  <? super Apple> 表示这个集合内元素的下界是Apple,
         * 所以向集合中添加 Apple 以及 Apple 的子类型是安全的，不会破坏这个承诺语义。
         * 但是添加 Fruit 就会报错。
         */
        List<? super Apple> superAppleList = new ArrayList<>();
        // ok
        superAppleList.add(new Apple());
        // 编译报错
        // superAppleList.add(new Fruit());
    }


    /**
     * 自定义一个水果父类
     */
    private static class Fruit {

    }
    /**
     * 定义一个苹果类继承自水果父类
     */
    private static class Apple extends Fruit {

    }
}
