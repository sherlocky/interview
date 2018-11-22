/**
 * 
 */
package com.sherlocky.interview.javase.guava;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;

import org.junit.Test;

/**
 * 字符串处理
 * @author zhangcx
 * @date 2018-05-11
 */
public class GuavaStringTest {

    @Test
    public void testCaseFormat() {
        // 大小写：
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CONSTANT_NAME"));
    }
    
    @Test
    public void testCharsets() {
        // 字符集：不需要做异常处理
        System.out.println("我是中文".getBytes(Charsets.UTF_8));
    }
    
    @Test
    public void testCharMatcher() {
        System.out.println(CharMatcher.any().collapseFrom("中            华人民 共  和   国", 'x'));
        // 把字符串中的连续空白字符替换为单个空格。
        System.out.println(CharMatcher.whitespace().trimAndCollapseFrom("中            华人民 共  和   国", 'x'));
        
        System.out.println(CharMatcher.digit().retainFrom("mahesh123")); // only the digits
        // trim whitespace at ends, and replace/collapse whitespace into single spaces
        System.out.println(CharMatcher.javaDigit().replaceFrom("mahesh123", "*")); // star out all digits
        System.out.println(CharMatcher.javaLetterOrDigit().retainFrom("mahesh123"));
        System.out.println(CharMatcher.javaDigit().or(CharMatcher.javaLowerCase()).retainFrom("mahesh123"));
        // eliminate all characters that aren't digits or lowercase
    }
    
    /**
    collapseFrom(CharSequence, char):把每组连续的匹配字符替换为特定字符。如
    matchesAllOf(CharSequence): 测试是否字符序列中的所有字符都匹配。
    removeFrom(CharSequence): 从字符序列中移除所有匹配字符。
    retainFrom(CharSequence): 在字符序列中保留匹配字符，移除其他字符。
    trimFrom(CharSequence): 移除字符序列的前导匹配字符和尾部匹配字符。
    replaceFrom(CharSequence, CharSequence):用特定字符序列替代匹配字符
    */
}
