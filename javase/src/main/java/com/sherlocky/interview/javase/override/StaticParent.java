package com.sherlocky.interview.javase.override;

public class StaticParent {
	public static String staticStr = "父类 - 静态属性";
	public String nonStaticStr = "父类 - 非静态属性";

	public static void staticMethod() {
		System.out.println("父类 - 静态方法");
	}

	public void nonStaticMethod() {
		System.out.println("父类 - 非静态方法");
	}
}
