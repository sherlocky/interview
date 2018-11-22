/**
 * 
 */
package com.sherlocky.interview.javase.singleton;

/**
 * static 代码块实现单例模式
 * @author zhangcx
 * @date 2018-08-23
 */
public class StaticCodeBlockSingleton {
    private static StaticCodeBlockSingleton instance = null;
    
    // 私有构造方法
    private StaticCodeBlockSingleton() {}
    
    static {
        instance = new StaticCodeBlockSingleton();
    }
    
    public static StaticCodeBlockSingleton getInstance() {
        return instance;
    }
}
