package com.sherlocky.interview.javase.annotation;

/**
 * 注解的使用 演示
 * @author: zhangcx
 * @date: 2019/9/5 19:41
 */
public class UseSherlockAnnotation {
    @SherlockAnnotation(id = 1, name = "Sherlock")
    private String demo;

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
