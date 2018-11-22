package com.sherlocky.interview.javase.guava;

import com.google.common.base.Preconditions;

import org.junit.Test;

/**
 *  Preconditions，前置条件判断
    没有额外参数：抛出的异常中没有错误消息；
    1.有一个Object对象作为额外参数：抛出的异常使用Object.toString() 作为错误消息；
    2.有一个String对象作为额外参数，并且有一组任意数量的附加Object对象：这个变种处理异常消息的方式有点类似printf，
    但考虑GWT的兼容性和效率，只支持%s指示符。
 * @author zhangcx
 * @date 2018-05-11
 */
public class GuavaPreconditionsTest {

    @Test
    public void testCheckArgument() {
        int i = -5;
        int j = -14;
        // IllegalArgumentException
        // Preconditions.checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
        // Preconditions.checkArgument(i >= 0, "Argument was but expected nonnegative", i);
        Preconditions.checkArgument(i < j, "Expected i < j, but %s > %s", i, j);
    }
    
    @Test
    public void testCheckNotNull() {
        Integer j = null;
        // NullPointerException
        Preconditions.checkNotNull(j, "j 不能为空");
    }
    
    @Test
    public void testCheckElementIndex() {
        // 索引值
        // IndexOutOfBoundsException
        Preconditions.checkElementIndex(1, 1, "越界了~");
    }    
    
    @Test
    public void testCheckPositionIndex() {
        // 位置值
        // IndexOutOfBoundsException
        Preconditions.checkPositionIndex(1, 1, "越界了~");
        // 还可以指定范围 使用 checkPositionIndexes
    }
}
