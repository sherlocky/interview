package com.sherlocky.interview.javase.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JDk7 下使用 Collections.sort 手动实现的 Comparator
 * 如果不满足 自反性、传递性、对称性时，会报错 Comparison method violates its general contract
 * <p>
 * JDK7+ 默认使用了 TimSort，不支持不严谨的 compare 逻辑了，可参考： {@link Arrays#sort(Object[], Comparator)}
 * <p>解决办法：
 * <ul>
 * <li>
 *     1： 切换回旧版JDK6的排序方法， 添加JVM参数 -Djava.util.Arrays.useLegacyMergeSort=true
 * </li>
 * <li>
 *     2： 修改 compare 实现，满足上述特性（逻辑必须严谨）,建议使用JDK实现好的
 * </li>
 * </ul>
 */
public class ComparatorTest {
    public static void main(String[] args) {
        // 网上摘抄了一个 可以复现该问题的 int 数组
        int[] ages = {0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 2, 1, 0, 0, 0, 2, 30, 0, 3};
        List<User> us = new ArrayList<>();
        Arrays.stream(ages).forEach(age -> us.add(new User(age)));
        Collections.sort(us, (o1, o2) -> {
            /**
             * 自定义实现的 Comparator 应当满足如下要求：
             * 1） 自反性： x ， y 的比较结果和 y ， x 的比较结果相反。
             * 2） 传递性： x > y , y > z ,则 x > z 。
             * 3） 对称性： x = y ,则 x , z 比较结果和 y ， z 比较结果相同。
             */
            /**
             * 错误的实现（逻辑不严谨）,在特定场景下会报：
             * java.lang.IllegalArgumentException: Comparison method violates its general contract!
             */
            return o1.getAge() > o2.getAge() ? 1 : -1;
            //return Integer.compare(o1.getAge(), o2.getAge());
        });
        us.forEach(System.out::println);
    }

    public static class User {
        private int age;

        public User(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    '}';
        }
    }
}
