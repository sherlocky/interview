package com.sherlocky.interview.javase.aop;

/**
 * 
 * @author Sherlocky
 * @date 2016年4月3日
 */
public class PeopleTalk implements ITalk {
	public String username;
	public String age;

	public PeopleTalk() {
		super();
	}

	public PeopleTalk(String username, String age) {
		super();
		this.username = username;
		this.age = age;
	}

	@Override
	public void talk(String msg) {
		System.out.println("I am " + username + ", 我今年" + age + "岁了!");
		System.out.println("I say ：" + msg);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
