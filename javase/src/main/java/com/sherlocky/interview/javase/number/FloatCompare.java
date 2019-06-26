package com.sherlocky.interview.javase.number;

import java.math.BigDecimal;

/**
 * 浮点数之间的等值判断比较特殊，基本数据类型不能用==来比较，包装数据类型不能用 equals 来判断。
 * <p>
 * 浮点数采用“ 尾数+阶码” 的编码方式，类似于科学计数法的“ 有效数字+指数” 的表示方式。
 * 二进制无法精确表示大部分的十进制小数，具体原理参考《码出高效》。
 * </p>
 *
 * @author: zhangcx
 * @date: 2019/6/26 8:52
 */
public class FloatCompare {
    public static void main(String[] args) {
        float a = 1.0f - 0.9f;
        float b = 0.9f - 0.8f;
        if (a == b) {
            // 预期进入此代码快，执行其它业务逻辑
            // 但事实上 a==b 的结果为 false
        } else {
            System.out.println("a == b 为: false");
        }
        Float x = Float.valueOf(a);
        Float y = Float.valueOf(b);
        if (x.equals(y)) {
            // 预期进入此代码快，执行其它业务逻辑
            // 但事实上 equals 的结果为 false
        } else {
            System.out.println("x.equals(y) 为: false");
        }

        /**
         * 正确的比较方式应该是：
         * 指定一个误差范围，两个浮点数的差值在此范围之内，则认为是相等的
         */
        float diff = 1e-6f;
        if (Math.abs(a - b) < diff) {
            System.out.println("true");
        }
        /**
         * 或者使用 BigDecimal 来定义值，再进行浮点数的运算操作
         */
        BigDecimal ba = new BigDecimal("1.0");
        BigDecimal bb = new BigDecimal("0.9");
        BigDecimal bc = new BigDecimal("0.8");
        BigDecimal bx = ba.subtract(bb);
        BigDecimal by = bb.subtract(bc);
        if (x.equals(y)) {
            System.out.println("true");
        }
    }
}
