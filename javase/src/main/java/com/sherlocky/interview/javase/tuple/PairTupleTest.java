package com.sherlocky.interview.javase.tuple;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 二元组
 * <p>元组 - tuple</p>
 * <p>{@link org.apache.commons.lang3.tuple.Pair Pair} 已经提供了二元组的实现</p>
 *
 * <p>{@link org.apache.commons.lang3.tuple.ImmutablePair ImmutablePair} -- 不可变的二元组</p>
 * <p>{@link org.apache.commons.lang3.tuple.MutablePair MutablePair} -- 可变的二元组</p>
 *
 * @author: zhangcx
 * @date: 2019/10/24 14:51
 */
public class PairTupleTest {

    public static void main(String[] args) {
        /** 可变 */
        MutablePair<String, Integer> person = getPerson();
        print(person);
        // 此处setRight 和 setValue 等价
        person.setRight(20);
        person.setValue(20);
        print(person);
        /** 不可变 */
        ImmutablePair<String, Integer> person2 = getPerson2();
        print(person2);
        // java.lang.UnsupportedOperationException
        // person2.setValue(20);
    }

    private static MutablePair<String, Integer> getPerson() {
        return new MutablePair<>("张三", 18);
    }

    private static ImmutablePair<String, Integer> getPerson2() {
        return new ImmutablePair<>("张三", 18);
    }

    private static void print(Pair<String, Integer> person) {
        System.out.println(String.format("姓名：%s, 年龄：%d", person.getLeft(), person.getRight()));
    }
}
