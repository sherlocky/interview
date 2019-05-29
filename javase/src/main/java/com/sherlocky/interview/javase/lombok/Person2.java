package com.sherlocky.interview.javase.lombok;

import com.google.common.collect.Maps;
import lombok.*;

import java.util.Map;

/**
 * lombok 注解 @Builder 快速实现构造器
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 默认的构造方法是default的
@ToString
@EqualsAndHashCode
@Getter
public class Person2 {
    private Integer id;
    private String name;
    private Boolean sex;
    private Map<String, String> info = Maps.newHashMap();
    @Builder.Default
    private Map<String, String> info2 = Maps.newHashMap();
    final private Map<String, String> info3 = Maps.newHashMap();
}