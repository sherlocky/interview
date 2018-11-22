package com.sherlocky.interview.multithread.condition.test;

import com.sherlocky.interview.multithread.condition.extthread.MyThreadA;
import com.sherlocky.interview.multithread.condition.extthread.MyThreadB;
import com.sherlocky.interview.multithread.condition.service.MyService;

public class Run {

	public static void main(String[] args) throws InterruptedException {
		MyService service = new MyService();

		int len = 1;
		
		MyThreadA[] threadA = new MyThreadA[len];
		MyThreadB[] threadB = new MyThreadB[len];

		for (int i = 0; i < len; i++) {
			threadA[i] = new MyThreadA(service);
			threadB[i] = new MyThreadB(service);
			threadA[i].start();
			threadB[i].start();
		}

	}
}
