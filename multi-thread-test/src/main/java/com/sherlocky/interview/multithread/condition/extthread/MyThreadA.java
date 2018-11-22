package com.sherlocky.interview.multithread.condition.extthread;

import com.sherlocky.interview.multithread.condition.service.MyService;

public class MyThreadA extends Thread {

	private MyService myService;

	public MyThreadA(MyService myService) {
		super();
		this.myService = myService;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			myService.set();
		}
	}

}
