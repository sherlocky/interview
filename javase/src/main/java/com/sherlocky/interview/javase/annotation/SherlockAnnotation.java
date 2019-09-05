package com.sherlocky.interview.javase.annotation;
import java.lang.annotation.*;

/**
 * Annotation（注解）是 Java 提供的一种对元程序中元素关联信息和元数据（ metadata）的途径和方法。
 *
 * <p>是一个接口，程序可以通过反射来获取指定程序中元素的 Annotation 对象，
 * 然后通过该 Annotation 对象来获取注解中的元数据信息</p>
 *
 * @author: zhangcx
 * @date: 2019/9/5 19:32
 */
/** 元注解 @Retention 定义了该 Annotation 被保留的时间长短(生命周期) */
@Retention(RetentionPolicy.RUNTIME)
/** 元注解 @Target 说明了Annotation所修饰的对象范围 */
@Target(ElementType.FIELD)
/**
 * @Inherited 元注解是一个标记注解，阐述了某个被标注的类型是被继承的。
 * 如果一个使用了 @Inherited 修饰的 Annotation 类型被用于一个 class，则这个 Annotation 将被用于该 class 的子类。
 */
/**
 * 元注解 @Documented 用于描述其它类型的 Annotation 应该被作为被标注的程序成员的公共 API，
 * 因此可以被例如 javadoc 此类的工具文档化。
 */
@Documented
public @interface SherlockAnnotation {
    /**
     * 默认 id
     * @return
     */
    public int id() default -1;
    /**
     * 默认名称
     * @return
     */
    public String name() default "";
}
