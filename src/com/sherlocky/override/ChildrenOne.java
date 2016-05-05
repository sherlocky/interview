package com.sherlocky.override;

public class ChildrenOne extends StaticParent {
	public static String staticStr = "子类One - 改写后的静态属性";
	public String nonStaticStr = "子类One - 改写后的非静态属性";

	public static void staticMethod() {
		System.out.println("子类One - 改写后的静态方法");
	}
	
	public void nonStaticMethod() {
		System.out.println("子类One - 非静态方法");
	}
}
