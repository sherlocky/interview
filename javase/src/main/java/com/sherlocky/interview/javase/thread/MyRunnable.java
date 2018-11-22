package com.sherlocky.interview.javase.thread;

/**
 * 
 * @author Sherlocky
 * @date 2016年4月28日
 */
public class MyRunnable implements Runnable {
	private String name;

	public MyRunnable(String name) {
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println("线程开始：" + this.name + ",i=" + i);
		}
	}
}
