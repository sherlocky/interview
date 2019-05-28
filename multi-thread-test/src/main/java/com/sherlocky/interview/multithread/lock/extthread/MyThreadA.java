package com.sherlocky.interview.multithread.lock.extthread;

import com.sherlocky.interview.multithread.lock.service.MyService;

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
