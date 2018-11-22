package com.sherlocky.interview.javase.string;


import org.junit.Test;

/**
 * @author zhangcx
 * @date 2017-09-05
 */
public class StringUnitTest {
    @Test
    public void test() {
        String s31 = new String("1") + new String("1");  
        s31.intern();  
        String s41 = "11";  
        System.out.println(s31 == s41);  // false
        
        String s3 = new String("15689") + new String("23740");  
        s3.intern();  
        String s4 = "1568923740";  
        System.out.println(s3 == s4);  // true
        
        String s7 = new String("fuck") + new String("you");  
        s7.intern();  
        String s8 = "fuckyou";  
        System.out.println(s7 == s8);  // true

        // java 关键字
        String s5 = new String("ja") + new String("va");  
        s5.intern();  
        String s6 = "java";  
        System.out.println(s5 == s6);   // false        
    }
}
