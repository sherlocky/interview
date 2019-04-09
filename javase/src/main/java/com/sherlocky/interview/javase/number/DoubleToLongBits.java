package com.sherlocky.interview.javase.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * java.lang.Double.doubleToLongBits() 方法返回根据IEEE754浮点“双精度格式”位布局，返回指定浮点值的表示。
 * <p>类比的有：Float.floatToIntBits() 方法</p>
 *
 * <ul>
 *  <li>如果参数是正无穷大, 其结果为 0x7ff0000000000000L</li>
 *  <li>如果参数是负无穷大, 其结果为 0xfff0000000000000L</li>
 *  <li>如果参数为 NaN, 其结果为 0x7ff8000000000000L</li>
 * <ul/>
 * <p>此方法返回表示浮点数位</p>
 * 在所有情况下，该方法结果都是一个 long 整数，
 * 将其赋予 longBitsToDouble(long) 方法将生成一个与 doubleToLongBits 的参数相同的浮点值（所有 NaN 值被压缩成一个“规范”NaN 值时除外）
 *
 * @author: zhangcx
 * @date: 2019/4/9 13:47
 */
public class DoubleToLongBits {
    public static void main(String[] args) {
        System.out.println("—————————————Double直接简单比较————————————————");
        Double d1 = 123.4567890D;
        Double d2 = new Double(123.4567890D);
        Double d3 = new Double(123.4567890D);
        System.out.println(d1.equals(d2));
        System.out.println(d1.equals(d3));
        System.out.println(d2.equals(d3));

        System.out.println("————————————————————Double >=17位比较 【会丢失精度】—————————");
        /** double比较，double 类型有效位是15~16位，绝对能保证的为 15 位，最多有 16 位
         * 同样的：float 类型有效位是6~7位，绝对能保证的为 6 位，最多有 7 位
         * */
        double x = 3.000000000000003;//16位
        double y = 30.00000000000003;
        double z = 300.0000000000003;
        double w = 3000.000000000003;
        System.out.println("3.000000000000003 toString ==> " + Double.toString(x));
        System.out.println("30.00000000000003 toString ==> " + Double.toString(y));
        System.out.println("30.00000000000003 toString ==> " + Double.toString(z));
        System.out.println("3000.000000000003 toString ==> " + Double.toString(w) + " // 刚好16位，精度不丢失");
        System.out.println("--------------------分界线-------------------");
        // 小数后都加一个0
        double x1 = 3.0000000000000003;//17位
        double y1 = 30.000000000000003;
        double z1 = 300.00000000000003;
        double w1 = 3000.0000000000003;
        System.out.println("3.0000000000000003 toString ==> " + Double.toString(x1));
        System.out.println("30.000000000000003 toString ==> " + Double.toString(y1));
        System.out.println("30.000000000000003 toString ==> " + Double.toString(z1));
        System.out.println("3000.0000000000003 toString ==> " + Double.toString(w1) + " // 超过16位，精度丢失");
        System.out.println("——————————————————doubleToLongBits——————————");
        System.out.println(Double.doubleToLongBits(123.4567890) == Double.doubleToLongBits(123.4567890));
        System.out.println(Double.doubleToLongBits(123.4567) > Double.doubleToLongBits(123.456));
        System.out.println(Double.doubleToLongBits(123.456) < Double.doubleToLongBits(123.4567));
        System.out.println("--------------------分界线-------------------");
        //returns the bits that represent the floating-point number
        System.out.println("Value = " + Double.doubleToLongBits(5.5d));
        System.out.println("Value = " + Double.longBitsToDouble(4617878467915022336L));
        System.out.println("———————————doubleToLongBits >16位 【依然会丢失精度】———");
        // 我擦，即使使用 doubleToLongBits，超过16位之后，依然会丢失精度。。。
        System.out.println(Double.doubleToLongBits(x) == Double.doubleToLongBits(3)); // false
        System.out.println(Double.doubleToLongBits(3.00000000000000003) == Double.doubleToLongBits(3)); // true

        System.out.println("—————————————————使用 BigDecimal ——————————————————————");
        double da = 2.85;
        double db = 2.01;
        System.out.println("da + db = " + (da + db)); // 4.859999999999999
        BigDecimal num1 = new BigDecimal(1.341);
        BigDecimal num2 = new BigDecimal(1.341);
        BigDecimal num3 = num1.add(num2);
        System.out.println("num3 = " + num3); // 2.6819999999999999396038674603914842009544372558593750
        System.out.println("BigDecimal 直接使用 double 类型参数还是会有精度问题！");
        BigDecimal num11 = BigDecimal.valueOf(1.341);
        BigDecimal num22 = BigDecimal.valueOf(1.341);
        BigDecimal num33 = num11.add(num22);
        System.out.println("--------使用 valueOf (暂时不丢失) --- 不建议使用，本质为Double.toString()，超过16位数【也会丢失精度】");
        System.out.println("num33 = " + num33);
        /** 解决办法，指定精度 */
        DecimalFormat df  = new DecimalFormat("0.000");
        double dd1 = 1.341;
        double dd2 = 1.341;
        BigDecimal num111 = new BigDecimal(df.format(dd1));
        BigDecimal num222 = new BigDecimal(df.format(dd2));
        BigDecimal num333 = num111.add(num222);
        System.out.println("--------使用 DecimalFormat.format() 的结果入参，【超过17位，依然会丢失精度】");
        System.out.println("num333 = " + num333);
        System.out.println("当超过17位时依然有问题，这是double型本身的精度问题导致的。。");
        DecimalFormat df2  = new DecimalFormat("0.00000000000000000");
        BigDecimal numX = new BigDecimal(df2.format(x1)); // 其实在 df2.format 时已经丢失了精度。。
        BigDecimal numXX = numX.add(numX);
        System.out.println("numX = " + numXX + "// 期望得到 6.00000000000000006");

        System.out.println("--------使用 new BigDecimal(\"xxxx\") 字符传入参，【推荐使用！！！】");

        BigDecimal num1111 = new BigDecimal("3.00000000000000003");
        BigDecimal num3333 = num1111.add(num1111);
        System.out.println("18位有效位小数字符串相加 num3333 = " + num3333 + "未丢失精度！");
    }
}
