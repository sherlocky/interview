package com.sherlocky.interview.javase.compare;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 快速实现比较器
 * {@link org.apache.commons.lang3.builder.CompareToBuilder CompareToBuilder}
 *
 * @author: zhangcx
 * @date: 2019/10/24 17:25
 * @since:
 */
public class CompareToBuilderTest {
    public static void main(String[] args) {

        List<ImmutablePair<String, Integer>> persons = Lists.newArrayList(
                new ImmutablePair("张一", 18),
                new ImmutablePair("张二", 18),
                new ImmutablePair("张三", 18),
                new ImmutablePair("张三", 20),
                new ImmutablePair("张三", 17));

        System.out.println(persons);

        Collections.sort(persons, new Comparator<ImmutablePair>() {
            @Override
            public int compare(ImmutablePair o1, ImmutablePair o2) {
                return new CompareToBuilder()
                        .append(o1.getLeft(), o2.getLeft())
                        .append(o1.getRight(), o2.getRight())
                        .toComparison();
            }
        });

        System.out.println(persons);
        // 还可以以Lambda形式实现
        Collections.sort(persons, (ImmutablePair o1, ImmutablePair o2) -> {
                return new CompareToBuilder()
                        .append(o2.getLeft(), o1.getLeft())
                        .append(o2.getRight(), o1.getRight())
                        .toComparison();
            }
        );
        System.out.println(persons);

        Collections.sort(persons, (ImmutablePair o1, ImmutablePair o2)
                ->
                new CompareToBuilder()
                        .append(o1.getRight(), o2.getRight())
                        .append(o1.getLeft(), o2.getLeft())
                        .toComparison()
        );

        System.out.println(persons);
    }
}