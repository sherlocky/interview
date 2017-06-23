/**
 * 
 */
package com.sherlocky.string;

import java.io.UnsupportedEncodingException;

/**
 * String.intern() 方法
 * @author zhangcx
 * @date 2017-06-23
 */
public class StringInternTest {
	private static String STATIC_PRIVATE = "STATIC_PRIVATE";
	public static String STATIC_PUBLIC = "STATIC_PUBLIC";
	
	private void testPrivate() {
		// 
	}
	
	public void testPublic() {
		// 
	}
	
	public static void main2(String[] args) throws UnsupportedEncodingException {
		System.out.println("I'am main2");
	}

	/**
	 * jdk 1.6下 以下全部为 false
	 */
	public static void main(String[] args) {
		// true
		String s1 = new StringBuffer("go").append("od").toString();
		System.out.print("good ");
		System.out.println(s1.intern() == s1);
		// false -- java 加载方法时已放到字符串常量池
		String s2 = new StringBuffer("ja").append("va").toString();
		System.out.print("java ");
		System.out.println(s2.intern() == s2);
		// false -- main 加载方法时已放到字符串常量池
		String s3 = new StringBuffer("ma").append("in").toString();
		System.out.print("main ");
		System.out.println(s3.intern() == s3);
		
		// false
		String s4 = new StringBuffer("STATIC").append("_PUBLIC").toString();
		System.out.print("STATIC_PUBLIC ");
		System.out.println(s4.intern() == s4);
		// false
		String s41 = new StringBuffer("STATIC").append("_PRIVATE").toString();
		System.out.print("STATIC_PRIVATE ");
		System.out.println(s41.intern() == s41);	
		
		/**
		 * jdk 1.7 false 类的全限定名会初始化到字符串常量池
		 * jdk 1.8 true 类的全限定名不会初始化到字符串常量池
		 */
		String s5 = new StringBuffer("com.sherlocky.string.").append("StringInternTest").toString();
		System.out.print("com.sherlocky.string.StringInternTest ");
		System.out.println(s5.intern() == s5);
		// false 类会初始化到字符串常量池
		String s51 = new StringBuffer("StringInternTest").toString();
		System.out.print("StringInternTest ");
		System.out.println(s51.intern() == s51);
		
		// true 私有方法名不会初始化到字符串常量池
		String s6 = new StringBuffer("test").append("Private").toString();
		System.out.print("testPrivate ");
		System.out.println(s6.intern() == s6);
		// false 公有方法名会初始化到字符串常量池
		String s7 = new StringBuffer("test").append("Public").toString();
		System.out.print("testPublic ");
		System.out.println(s7.intern() == s7);

	}
}
