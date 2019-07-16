package com.sherlocky.interview.javase._enum;

/**
 * 枚举类反编译后本质是一个类。
 *
 * <p>本例只做演示使用，java不允许我们自己继承 java.lang.Enum 类！</p>
 *
 * @author: zhangcx
 * @date: 2019/7/16 14:38
 */
public final class EnumFruitClass extends Enum {

    /**
     * 私有的构造方法
     * @param name
     * @param ordinal
     */
    private EnumFruitClass(String name, int ordinal) {
        super(name, ordinal);
    }

    public static final EnumFruitClass APPLE;
    public static final EnumFruitClass BANANA;
    /** 樱桃 */
    public static final EnumFruitClass CHERRY;
    /** 榴莲 */
    public static final EnumFruitClass DURIAN;
    /** 茄子 */
    public static final EnumFruitClass EGGPLANT;
    /** 葡萄 */
    public static final EnumFruitClass GRAPE;
    private static final EnumFruitClass $VALUES[];

    static {
        APPLE = new EnumFruitClass("APPLE", 0);
        BANANA = new EnumFruitClass("BANANA", 1);
        CHERRY = new EnumFruitClass("CHERRY", 2);
        DURIAN = new EnumFruitClass("DURIAN", 3);
        EGGPLANT = new EnumFruitClass("EGGPLANT", 4);
        GRAPE = new EnumFruitClass("GRAPE", 5);
        $VALUES = new EnumFruitClass[] {
                APPLE, BANANA, CHERRY, DURIAN, EGGPLANT, GRAPE
        };
    }

    public static EnumFruitClass[] values() {
        return $VALUES;
    }

    public static EnumFruitClass valueOf(String name) {
        return (EnumFruitClass) Enum.valueOf(EnumFruitClass.class, name);
    }

}

/**
 * 由于Java不允许我们自己继承 Enum，故为了演示需要，此处自行实现一个假的 Enum。
 */
class Enum<E extends Enum<E>> {
    private final String name;
    private final int ordinal;
    public final String name() {
        return name;
    }
    public final int ordinal() {
        return ordinal;
    }

    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public static <T extends Enum> T valueOf(Class<T> enumType, String name) {
        // 此处只做演示，无需实现
        return null;
    }

    /**
     * 。。。。。
     * 只做演示，其他方法省略！
     */
}