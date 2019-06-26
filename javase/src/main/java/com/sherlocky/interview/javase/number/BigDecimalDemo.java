package com.sherlocky.interview.javase.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal 零值比较，舍入等
 * @author: zhangcx
 * @date: 2019/6/26 8:51
 */
public class BigDecimalDemo {
    public static void main(String[] args) {
        BigDecimal total = new BigDecimal(0);
        BigDecimal total2 = BigDecimal.ZERO;
        System.out.println(total == total2); // false
        System.out.println(total.equals(total2)); // ttue
        System.out.println(total.compareTo(total2)); // 0

        BigDecimal devide = BigDecimal.valueOf(3.14);
        // 保留6位小数，舍入
        /**
         * 8 种舍入方式：
         * ROUND_UP 远离零的方向舍入，在精度最后一位加一个单位：1.666->1.67 1.011->1.02  1.010->1.01，该模式永远不会减少被操作的数的值。
         *
         * ROUND_DOWN 靠近零的方向舍入，截端操作，类似truncate 该模式永远不会增加被操作的数的值。
         *
         * ROUND_CEILING 靠近正无穷方向舍入，如果为正数，行为和round_up一样，如果为负数，行为和round_down一样。
         *
         * ROUND_FLOOR 靠近负无穷方向舍入，如果为正数，行为和round_down一样，如果为负数，行为和round_up一样。
         *
         * ROUND_HALF_UP 四舍五入，生活中的舍入方法。遇到.5的情况时往上近似,例: 1.5 -> 2
         *
         * ROUND_HALF_DOWN 五舍六入。遇到.5的情况时往下近似,例: 1.5 -> 1
         *
         * ROUND_HALF_EVEN 精确舍入，银行家舍入法：四舍六入五成双。
         *      >四舍六入，五分两种情况：如果前一位为奇数，则入位，否则舍去，即保证末尾数字都成为偶数。
         *      若5的后面还有不为“0”的任何数，则此时无论5的前面是奇数还是偶数，均应进位。
         *      以下例子为保留小数点1位，那么这种舍入方式下的结果：1.15 -> 1.2    1.25 -> 1.2
         *
         * ROUND_UNNECESSARY 无需舍入
         */
        System.out.println(total2.divide(devide, 6, RoundingMode.CEILING));
        System.out.println(new BigDecimal(12345).divide(devide, 6, RoundingMode.CEILING));
        // 测试 ROUND_HALF_EVEN
        System.out.println("========ROUND_HALF_EVEN========");
        System.out.println(new BigDecimal("3.144").setScale(2, RoundingMode.HALF_EVEN)); // 3.14
        System.out.println(new BigDecimal("3.146").setScale(2, RoundingMode.HALF_EVEN)); // 3.15
        System.out.println(new BigDecimal("9.8350").setScale(2, RoundingMode.HALF_EVEN)); // 9.84
        System.out.println(new BigDecimal("9.8250").setScale(2, RoundingMode.HALF_EVEN)); // 9.82
        System.out.println(new BigDecimal("3.115").setScale(2, RoundingMode.HALF_EVEN)); // 3.12
        System.out.println(new BigDecimal("3.125").setScale(2, RoundingMode.HALF_EVEN)); // 3.12
        System.out.println(new BigDecimal("30.1451").setScale(2, RoundingMode.HALF_EVEN)); // 30.15
        System.out.println(new BigDecimal("30.1351").setScale(2, RoundingMode.HALF_EVEN)); // 30.14
        System.out.println(new BigDecimal("3.14509").setScale(2, RoundingMode.HALF_EVEN)); // 3.15
        System.out.println(new BigDecimal("3.13509").setScale(2, RoundingMode.HALF_EVEN)); // 3.14
        System.out.println("========ROUND_HALF_EVEN========");

        // 0 不能做除数
        // System.out.println(devide.divide(total)); // ArithmeticException: Division by zero

        BigDecimal bd = new BigDecimal("0");
        BigDecimal bd2 = new BigDecimal("0.00");
        // 与 0.0 的比较使用 BigDecimal.ZERO
        System.out.println(bd.compareTo(BigDecimal.ZERO));
        System.out.println(bd.compareTo(new BigDecimal(0.00)));
        System.out.println(bd2.compareTo(BigDecimal.ZERO));
        System.out.println(bd2.compareTo(new BigDecimal(0.00)));
    }
}
