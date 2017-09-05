package com.sherlocky.string;

/**
 * 大前提【使用intern()比不使用intern()消耗的内存更少】
 */

/**
 * 在JVM运行时数据区中的方法区有一个【常量池】，但是在JDK1.7+开始常量池被放置在了【堆空间】，
 * 因此常量池位置的不同影响到了String的intern()方法的表现。
 */
/**
 * String.intern() 方法深入理解<p>
 * 
    1.在jdk1.6-的时候 intern() 时会在常量池中创建一个字符串对象（如果不存在的话），然后返回它的引用<p>
    2.在jdk1.7+的时候【由于常量池整个存在于堆中】 intern() 时，如果常量池中不存在对应字符串<p>
                      此时堆中是存在对应字符串对象的，因此常量池不需要再存储一份对象了，可以直接存储堆中的引用<p>
           ==>> <i>str.intern() == str</i><p>
 * 参考博文:【<a href="http://blog.csdn.net/seu_calvin/article/details/52291082">Java技术——你真的了解String类的intern()方法吗</a>】
 * @author zhangcx
 * @date 2017-09-04
 */
public class StringInternTest {


    
    // 以下四个方法必须单独测试，一起测试会有影响
    public static void main(String[] args) {
//        intern1();
//        intern2();
//        intern3();
//        intern4();
        intern5();
    }
    
    // jdk1.7+
    public static void intern1() {
        // 在常量池生成 "fuck" 和 "you"，并在对中生成s1指向的对象，内容为 "fuckyou"
        String s1 = new String("fuck") + new String("you");
        // s1.intern 发现常量池不存在 fuckyou，因此直接指向了s1，"fuckyou" 在常量池中创建时也就指向了s1
        System.out.println(s1.intern() == s1); // true
        System.out.println("fuckyou" == s1); // true
    }
    
    // jdk1.7+
    public static void intern2() {
        String s1 = new String("fuck") + new String("you");
        // 
        String s2 = "fuckyou";
        System.out.println(s1.intern() == s1); // false
        System.out.println(s1 == s2); // false
        System.out.println(s1.intern() == s2); // true
    }
    
    // jdk1.7+
    public static void intern3() {
        String s1 = new String("fuck") + new String("you");
        // 先 intern
        System.out.println(s1.intern() == s1); // true
        String s2 = "fuckyou";
        System.out.println(s1 == s2); // true
        System.out.println(s1.intern() == s2); // true
    }
    
    // jdk1.7+
    public static void intern4() {
        String s2 = "fuckyou";
        String s1 = new String("fuck") + new String("you");
        System.out.println(s1.intern() == s1); // false
        System.out.println(s1 == s2); // false
        System.out.println(s1.intern() == s2); // true
    }
    
    // jdk1.7+
    public static void intern5() {
        String s = new String("1");  
        s.intern();  
        String s2 = "1";  
        System.out.println(s == s2); // false
          
        String s3 = new String("1") + new String("1");  
        s3.intern();  
        String s4 = "11";  
        String s41 = new String("11");
        System.out.println(s3 == s4);  // true
        System.out.println("************");
        System.out.println(s41.intern() == s4);  // true
        System.out.println(s41 == s4);  // false
        System.out.println("************");
        
        String s7 = new String("fuck") + new String("you");  
        s7.intern();  
        String s8 = "fuckyou";  
        System.out.println(s7 == s8);  // true

        // java 关键字
        String s5 = new String("ja") + new String("va");  
        s5.intern();  
        String s6 = "java";  
        System.out.println(s5 == s6);   // false        
        
        // intern5 静态方法名
        String s9 = new String("intern") + new String("5");  
        s9.intern();  
        String s10 = "intern5";  
        System.out.println(s9 == s10);  // false

        // main 方法名
        String s11 = new String("ma") + new String("in");  
        s11.intern();  
        String s12 = "main";  
        System.out.println(s11 == s12);  // false
    }    
}
