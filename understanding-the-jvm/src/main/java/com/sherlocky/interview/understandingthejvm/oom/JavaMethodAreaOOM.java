package com.sherlocky.interview.understandingthejvm.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 借助CGLib使方法去出现内存溢出异常
 * 
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 * @author zhangcx
 * @date 2018-10-24
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        }
    }
    static class OOMObject {}
}
/**
运行结果：
Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
    at java.lang.Class.getDeclaredMethods0(Native Method)
    at java.lang.Class.privateGetDeclaredMethods(Class.java:2427)
    at java.lang.Class.getDeclaredMethod(Class.java:1935)
    at net.sf.cglib.proxy.Enhancer.getCallbacksSetter(Enhancer.java:627)
    at net.sf.cglib.proxy.Enhancer.setCallbacksHelper(Enhancer.java:615)
    at net.sf.cglib.proxy.Enhancer.setThreadCallbacks(Enhancer.java:609)
    at net.sf.cglib.proxy.Enhancer.createUsingReflection(Enhancer.java:631)
    at net.sf.cglib.proxy.Enhancer.firstInstance(Enhancer.java:538)
    at net.sf.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:225)
    at net.sf.cglib.proxy.Enhancer.createHelper(Enhancer.java:377)
    at net.sf.cglib.proxy.Enhancer.create(Enhancer.java:285)
    at oom.JavaMethodAreaOOM.main(JavaMethodAreaOOM.java:28)
**/