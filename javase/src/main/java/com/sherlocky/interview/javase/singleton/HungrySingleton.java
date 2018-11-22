/**
 * 
 */
package com.sherlocky.interview.javase.singleton;

/**
 * 饿汉模式 实现单例
 * @author zhangcx
 * @date 2018-08-23
 */
public class HungrySingleton {
    // 立即加载方式==饿汉模式
    private static HungrySingleton instance = new HungrySingleton();

    // 私有构造方法
    private HungrySingleton() {}

    public static HungrySingleton getInstance() {
        // 此代码版本为立即加载
        // 此版本代码的缺点是不能有其它实例变量
        // 因为getInstance()方法没有同步
        // 所以有可能出现非线程安全问题
        return instance;
    }
}
