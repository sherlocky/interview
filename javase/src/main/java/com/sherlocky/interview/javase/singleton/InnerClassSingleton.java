package com.sherlocky.interview.javase.singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 内部类方式实现单例
 * @author zhangcx
 * @date 2018-08-23
 */
public class InnerClassSingleton implements Serializable {
    private static final long serialVersionUID = 2049102434160727122L;

    // 内部类方式
    private static class InnerClassSingletonHandler {
        private static InnerClassSingleton instance = new InnerClassSingleton();
    }
    
    // 私有构造方法
    private InnerClassSingleton() {}
    
    public static InnerClassSingleton getInstance() {
        return InnerClassSingletonHandler.instance;
    }
    
    // 反序列化时使用 readResolve 方法可以解决反序列化后多例问题
    protected Object readResolve() throws ObjectStreamException {
        System.out.println("调用了readResolve方法！");
        return InnerClassSingletonHandler.instance;
    }
}
