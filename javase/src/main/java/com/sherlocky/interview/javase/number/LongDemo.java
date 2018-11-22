package com.sherlocky.interview.javase.number;
/** 
* Long cache studing
* @author Sherlocky
* @date 2016年4月28日
*/
public class LongDemo {
	public static void main(String[] args) {
		long l1 = 1;
		Long l2 = 1L;
		Long l3 = 1L;
		Long l4 = 127L;
		Long l5 = 127L;
		Long l6 = 128L;
		Long l7 = 128L;
		Long l8 = -127L;
		Long l9 = -127L;
		Long l10 = -128L;
		Long l11 = -128L;
		Long l12 = -129L;
		Long l13 = -129L;
		System.out.println(l1 == 1);
		System.out.println(l2 == 1);
		System.out.println(l1 == l2);
		/**
		 *  long cache -128 ~ 127
		 *  -128 ~ 127时 jvm对Integer和Long会有缓存
		 *  此时即使new() 也会 ==
		 */
		System.out.println(l2 == l3);
		System.out.println(l4 == l5);
		System.out.println(l8 == l9);
		System.out.println(l10 == l11);
		// 超出long cache
		System.out.println(l6 == l7); // 128
		System.out.println(l12 == l13); // -129
	}
}
