package com.sherlocky.interview.javase.lombok;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * lombok 注解 @Data 等价于 @ToString、@EqualsAndHashCode、@Getter/@Setter、@RequiredArgsConstructor的所有特性
 */
@Data
@Accessors(chain = true)
public class Person {
    private Integer id;
    private String name;
    private Boolean sex;
    private Map<String, String> info;
}