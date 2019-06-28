package com.sherlocky.interview.javase.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * SimpleDateFormat 是线程不安全的类，一般不要定义为 static 变量，
 * 如果定义为 static，则必须加锁，或者使用 DateUtils 工具类。
 *
 * @author: zhangcx
 * @date: 2019/6/28 13:58
 */
public class SimpleDateFormatSample {
    /**
     * 推荐这样处理
     */
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    // 或者使用局部变量的方式


    /**
     * 另外，如果是 JDK8+
     * 可以使用 Instant 代替 Date，LocalDateTime 代替 Calendar，DateTimeFormatter 代替 SimpleDateFormat。
     * 官方给出的解释：simple beautiful strong immutable thread-safe.
     *
     * 示例代码可参考：sherlocky/java8-learning#com/sherlocky/learning/java8/datetimeapi/LocalDateTimeApiSample.java
     * <a href="https://gitee.com/sherlocky/java8-learning/blob/master/src/main/java/com/sherlocky/learning/java8/datetimeapi/LocalDateTimeApiSample.java"></a>
     */
}
