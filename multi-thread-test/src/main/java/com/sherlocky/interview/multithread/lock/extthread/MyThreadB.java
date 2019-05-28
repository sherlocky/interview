package com.sherlocky.interview.multithread.lock.extthread;

import com.sherlocky.interview.multithread.lock.service.MyService;

public class MyThreadB extends Thread {

	private MyService myService;

	public MyThreadB(MyService myService) {
		super();
		this.myService = myService;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			myService.get();
		}
	}

}
