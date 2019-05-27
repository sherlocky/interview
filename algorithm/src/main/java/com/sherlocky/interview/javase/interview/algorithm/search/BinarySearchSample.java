package com.sherlocky.interview.javase.interview.algorithm.search;

import java.util.Arrays;

/**
 * 二分查找 示例
 * jdk 原生实现了数组的二分查找：Arrays.binarySearch()
 * @author: zhangcx
 * @date: 2019/5/27 9:01
 */
public class BinarySearchSample {

    public static int binarySearch(int[] datas, int key) {
        if (datas.length == 0) {
            return -1;
        }
        // 查找范围起点
        int start = 0;
        // 查找范围终点
        int end = datas.length - 1;

        // 迭代二分查找
        while (start <= end) {
            // 查找范围中位数
            int mid = (start + end) >>> 1; // 无符号右移一位（除以2）
            int midVal = datas[mid];
            if (midVal == key) {
                return mid;
            }
            if (midVal > key) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] datas = new int[] { 12, 35, 39, 42, 59, 77, 90, 102, 159, 203 };
        System.out.println("## 手动实现：");
        long start = System.nanoTime();
        System.out.println(binarySearch(datas, 35));
        long end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");
        System.out.println("## jdk原生实现：");
        start = System.nanoTime();
        // 有非空检测和下标范围检测
        System.out.println(Arrays.binarySearch(datas, 35));
        end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");
    }
}
