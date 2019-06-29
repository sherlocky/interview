package com.sherlocky.interview.javase._switch;

/**
 * JDK7+ 支持 String 作为 switch 参数必须注意 NPE 问题
 *
 * @author: zhangcx
 * @date: 2019/6/29 12:27
 */
public class SwitchString {

    public static void main(String[] args) {
        testSwitch("p1");
        testSwitch("null");
        testSwitch("p2");
        /**
         * 此处会报：java.lang.NullPointerException
         * 【强制】当switch 括号内的变量类型为 String并且此变量为外部参数时，
         * 必须先进行 null 判断。
         */
        testSwitch(null);
    }

    private static void testSwitch(String param) {
        switch (param) {
            case "p1":
                System.out.println("It's " + param);
                break;
            case "null":
                System.out.println("It's NULL！");
                break;
            default:
                System.out.println("default~");
        }
    }

}
