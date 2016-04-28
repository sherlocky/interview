package com.sherlocky.aop.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理类
 * @author Sherlocky
 * @date 2016年4月4日
 */
public class DynamicTalkProxy implements InvocationHandler {
	/** 需要代理的目标类 */
	private Object target;
	
	/**
	 * 写法固定，aop专用:绑定委托对象并返回一个代理类
	 * @param target
	 * @return
	 * @author Sherlocky
	 * @date 2016年4月4日
	 */
	public Object bind(Object target) {
		this.target = target;
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
        // 切面之前执行
        System.out.println("切面之前执行");
        // 执行业务
        result = method.invoke(target, args);
        // 切面之后执行
        System.out.println("切面之后执行");
        return result;
	}
}
