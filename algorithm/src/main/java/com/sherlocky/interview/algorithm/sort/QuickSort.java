package com.sherlocky.interview.algorithm.sort;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * 快速排序
 * <p>也属于交换排序，之所以快速是因为使用了【分治法】。</p>
 * <p>
 * 冒泡排序在每一轮只把一个元素冒泡到数列的一端，而快速排序在每一轮挑选一个【基准元素】，
 * 并让其他比它大的元素移动到数列一边，比它小的元素移动到数列的另一边，
 * 从而把数列拆解成了两个部分。
 * </p>
 * <p>
 * 选定了基准元素以后，元素的移动有两种方法：
 * <ul>
 * <li>1.挖坑法</li>
 * <li>2.指针交换法</li>
 * </ul>
 * </p>
 * <p>平均时间复杂度：O（nlogn），极端情况下退化成：O（n^2）</p>
 *
 * <p>可参考：<a href="https://mp.weixin.qq.com/s?__biz=MzIxMjE5MTE1Nw==&mid=2653195042&idx=1&sn=2b0915cd2298be9f2163cc90a3d464da"/>漫画：什么是快速排序？（完整版）</p>
 * @author: zhangcx
 * @date: 2019/10/8 9:40
 */
public class QuickSort {
    /**
     * 挖坑法
     */
    static class Potholing {
        /**
         * 递归排序
         * @param array 待排序列
         * @param start  左边起始位置
         * @param end 右边结束位置
         */
        private static void quickSort(int[] array, int start, int end) {
            // 递归结束条件：start 大等于 end 的时候
            if (start >= end) {
                return;
            }
            // 得到基准元素位置
            int pivotIndex  = partition(array, start, end);
            //分别进行左右的递归
            quickSort(array, start, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, end);
        }

        private static int partition(int[] arr, int startIndex, int endIndex) {
            // 取第一个位置的元素作为基准元素
            int pivot = arr[startIndex];
            int left = startIndex;
            int right = endIndex;
            // 坑的位置，初始等于 pivot 的位置
            int index = startIndex;
            // 大循环在左右指针重合或者交错时结束
            while (right >= left) {
                // right 指针从右向左进行比较
                while (right >= left) {
                    if (arr[right] < pivot) {
                        arr[left] = arr[right];
                        index = right;
                        left++;
                        break;
                    }
                    right--;
                }
                // left指针从左向右进行比较
                while (right >= left) {
                    if (arr[left] > pivot) {
                        arr[right] = arr[left];
                        index = left;
                        right--;
                        break;
                    }
                    left++;
                }
            }
            arr[index] = pivot;
            return index;
        }
    }

    /**
     * 指针交换法
     */
    static class PointerExchange {
        /**
         * 递归排序
         *
         * @param array 待排序列
         * @param left  左边起始位置
         * @param right 右边结束位置
         */
        private static void quickSort(int[] array, int left, int right) {
            if (left < right) {
                //根据基准点，找到分隔左右子序列的位置索引
                int position = position(array, left, right);
                //分别进行左右的递归
                quickSort(array, left, position - 1);
                quickSort(array, position + 1, right);
            }
        }

        /**
         * 找到中间
         *
         * @param array 待排序列
         * @param left  左边起始位置
         * @param right 右边结束位置
         * @return
         */
        private static int position(int[] array, int left, int right) {
            //找到基准点, 这里使用的是序列的第一个元素
            int base = array[left];
            //  0  1  2  3  4  5  6  7  8  9  10 11 12
            // {2, 3, 1, 4, 7, 8, 3, 5, 2, 6, 8, 9, 1}
            while (left < right) {
                while (right > left && array[right] >= base) {
                    right--;
                }
                //交互位置
                array[left] = array[right];
                while (left < right && array[left] <= base) {
                    left++;
                }
                //交互位置
                array[right] = array[left];
            }
            //此时 left 与 right 是相等的
            array[left] = base;
            return left;
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{2, 3, 1, 4, 7, 8, 3, 5, 2, 6, 8, 9, 1};
        int[] array2 = ArrayUtils.clone(array);

        System.out.println("快速排序-指针交换法");
        System.out.println(Arrays.toString(array));
        PointerExchange.quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));

        System.out.println("快速排序-挖坑法");
        System.out.println(Arrays.toString(array2));
        Potholing.quickSort(array2, 0, array2.length - 1);
        System.out.println(Arrays.toString(array2));
    }
}