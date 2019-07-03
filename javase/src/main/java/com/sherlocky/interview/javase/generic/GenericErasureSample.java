package com.sherlocky.interview.javase.generic;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * 泛型擦除简介
 *
 * <p>Java泛型，基于擦除实现。</p>
 *
 *
 * @author: zhangcx
 * @date: 2019/7/3 14:03
 */
public class GenericErasureSample {
    /**
     * Error: both methods has the same erasure
     *
     * 编译时泛型擦除后，两个 say() 方法签名一样，报错！
     */
    // public void say(List wordList) {}
    public void say(List<String> wordList) {}

    public static void main(String[] args) {
        /**
         * 泛型编译后虽然被擦除了，但还能拿到泛型信息。
         * 类编译后的 class 字节码中会有个 signature 字段来保存泛型信息。
         */
        Class clz = new HashMap<String, Double>() {
        }.getClass();
        Type superType = clz.getGenericSuperclass();
        System.out.println(superType);
        if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            for (Type type : actualTypes) {
                System.out.println(type);
            }
        }
    }

}
