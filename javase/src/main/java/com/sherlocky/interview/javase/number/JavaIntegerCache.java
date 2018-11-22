package com.sherlocky.interview.javase.number;

/**
 * Java Integer 缓存代码示例
 * @date 2018/11/22
 */
public class JavaIntegerCache {
    public static void main(String... strings) {

        Integer integer1 = 3;
        Integer integer2 = 3; // ==
        // Integer integer2 = Integer.valueOf(3); // ==
        // Integer integer2 = new Integer(3); // !=

        if (integer1 == integer2) {
            System.out.println("integer1 == integer2");
        } else {
            System.out.println("integer1 != integer2");
        }

        Integer integer3 = 300;
        Integer integer4 = 300;

        if (integer3 == integer4) {
            System.out.println("integer3 == integer4");
        } else {
            System.out.println("integer3 != integer4");
        }
    }
}