package com.sherlocky.interview.javase.singleton;

/**
 * 使用枚举实现单例模式
 * @author zhangcx
 * @date 2018-08-23
 */
public class EnumSingletonClazz {

    public enum EnumSingleton {
        instanceFactory;
        
        // 此处使用 String 简化代码
        private String instance;
        
        private EnumSingleton() {
            System.out.println("创建 EnumSingletonClazz 对象");
            instance = new String("EnumSingletonClazz");
        }
        
        public String getInstance() {
            return instance;
        }
    }
    
    public static String getInstance() {
        return EnumSingleton.instanceFactory.getInstance();
    }
}
