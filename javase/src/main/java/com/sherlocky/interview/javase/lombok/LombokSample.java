package com.sherlocky.interview.javase.lombok;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

/**
 * @author: zhangcx
 * @date: 2019/5/29 13:39
 */

public class LombokSample {

    public static void main(String[] args) {
        /*********** RequiredArgsConstructor **************/
        Person p = new Person();
        /*********** getter/setter **************/
        p.getName();
        p.setId(123);
        // 使用 @Accessors(chain = true) 注解后，可以链式调用setter方法
        p.setName("123").setSex(true).setInfo(Maps.newHashMap());

        System.out.println(JSON.toJSONString(p));
        /*********** ToString **************/
        p.toString();
        System.out.println(p);
        /*********** EqualsAndHashCode **************/
        System.out.println(p.hashCode());
        System.out.println(p.equals(new Person()));


        /*********** @Builder **************/
        Person2 p2 = Person2.builder().id(123).name("123").sex(true).build();
        System.out.println(p2);
        /**
         * 如果使用@Builder的话切记所有私有全局变量都是需要显式赋值的，否则就是Null，
         * 不管你在原生T类中是否实例化，最终都是要被Builder的build()方法来重新实例化的
         */
        System.out.println("Persion.info=" + p2.getInfo());
        // 可以使用 @Builder.Default 注解（1.16.16+） 让自定义的默认值不被@Builder复写
        System.out.println("Persion.info2=" + p2.getInfo2());
        // 或者将私有全局变量定义为 final 的
        System.out.println("Persion.info3=" + p2.getInfo3());
        System.out.println(p2.hashCode());
        System.out.println(p2.equals(new Person()));
    }
}
