package com.sherlocky.interview.javase._enum;

/**
 * 枚举类型示例，枚举类反编译后本质内容可参考：{@linkplain EnumFruitClass }
 *
 * <p>阿里巴巴java开发规约明确强调：</p>
 * <p>【强制】 二方库里可以定义枚举类型，参数可以使用枚举类型，但是接口返回值不允许使用
 * 枚举类型或者包含枚举类型的 POJO 对象。</p>
 *
 * <p>主要原因是经过序列化之后，是不能再随意修改原来枚举类型中定义的的任何枚举对象的，否则会抛出异常！
 * 可参考 {@linkplain java.lang.Enum#valueOf(Class, String)} 。
 * </p>
 *
 * @author: zhangcx
 * @date: 2019/7/16 14:32
 */
public enum EnumFruit {

    APPLE,
    BANANA,
    /** 樱桃 */
    CHERRY,
    /** 榴莲 */
    DURIAN,
    /** 茄子 */
    EGGPLANT,
    /** 无花果 */
    FIG,
    /** 葡萄 */
    GRAPE;

    public static void main(String[] args) {
        System.out.println(EnumFruit.APPLE);
    }
}
