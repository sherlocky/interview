package com.sherlocky.interview.javase.aop.dynamic;

import com.sherlocky.interview.javase.aop.ITalk;
import com.sherlocky.interview.javase.aop.PeopleTalk;

/** 

* @author Sherlocky
* @date 2016年4月4日
*/
public class DynamicTalkProxyTest {
	public static void main(String[] args) {
		// 将业务接口和业务类绑定到动态代理类
		ITalk talker = (ITalk) new DynamicTalkProxy().bind(new PeopleTalk());
		talker.talk("动态代理msg");
	}
}
