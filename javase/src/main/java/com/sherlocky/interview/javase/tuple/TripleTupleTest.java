package com.sherlocky.interview.javase.tuple;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

/**
 * 三元组
 * <p>元组 - tuple</p>
 * <p>{@link org.apache.commons.lang3.tuple.Triple Triple} 已经提供了三元组的实现</p>
 *
 * <p>{@link org.apache.commons.lang3.tuple.ImmutableTriple ImmutableTriple} -- 不可变的三元组</p>
 * <p>{@link org.apache.commons.lang3.tuple.MutableTriple MutableTriple} -- 可变的三元组</p>
 * @author: zhangcx
 * @date: 2019/10/24 14:51
 */
public class TripleTupleTest {

    public static void main(String[] args) {
        /** 可变 */
        MutableTriple<String, Integer, Boolean> person = getPerson();
        print(person);
        person.setMiddle(20);
        print(person);
        /** 不可变 */
        ImmutableTriple<String, Integer, Boolean> person2 = getPerson2();
        print(person2);
    }

    private static MutableTriple<String, Integer, Boolean> getPerson() {
        return new MutableTriple<>("张三", 18, true);
    }

    private static ImmutableTriple<String, Integer, Boolean> getPerson2() {
        return new ImmutableTriple<>("张三", 18, true);
    }

    private static void print(Triple<String, Integer, Boolean> person) {
        System.out.println(String.format("姓名：%s, 年龄：%d, 性别：%s", person.getLeft(), person.getMiddle(), person.getRight() ? "男":"女"));
    }
}
