package com.sherlocky.interview.javase.annotation;
import java.lang.reflect.Field;

/**
 * @author: zhangcx
 * @date: 2019/9/5 19:44
 */
public class AnnotationUtils {
    public static void getAnnotationInfo(Class<?> clazz) {
        String val = "demo";
        // 通过反射获取注解
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SherlockAnnotation.class)) {
                SherlockAnnotation sa = (SherlockAnnotation) field.getAnnotation(SherlockAnnotation.class);
                val = String.format("ID: %s, name: %s.", sa.id(), sa.name());
                System.out.println(val);
            }
        }
    }

    public static void main(String[] args) {
        AnnotationUtils.getAnnotationInfo(UseSherlockAnnotation.class);
    }
}
