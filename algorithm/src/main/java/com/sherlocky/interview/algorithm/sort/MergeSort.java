package com.sherlocky.interview.algorithm.sort;

import java.util.Arrays;

/**
 * 归并排序
 * <p>主要分为两个阶段：</p>
 * <ul>
 *     <li>1.分组（逐层拆半分组，一直到每组只有一个元素为止）</li>
 *     <li>
 *         2.归并（每个小组内部比较出先后顺序以后，小组之间会展开进一步的比较和排序，合并成一个大组；
 *     大组之间继续比较和排序，再合并成更大的组......最终，所有元素合并成了一个有序的集合。）
 *     </li>
 * </ul>
 * <p>归并操作大致需要三个步骤：</p>
 * <ul>
 *     <li>1.创建一个额外大集合用于存储归并结果，长度是两个小集合之和。（p1，p2，p是三个辅助指针，用于记录当前操作的位置）</li>
 *     <li>2.从左到右逐一比较两个小集合中的元素，把较小的元素优先放入大集合。</li>
 *     <li>3.从另一个还有剩余元素的集合中，把剩余元素按顺序复制到大集合尾部。</li>
 * </ul>
 *
 * <p>是稳定排序算法，类似擂台比武大赛（区别在于：擂台赛只需要决出第一名，归并排序要确定每一个元素的位置）</p>
 * <p>时间复杂度：O（nlogn） 空间复杂度：O（n）</p>
 *
 * <p>可参考：<a href="https://mp.weixin.qq.com/s?__biz=MzIxMjE5MTE1Nw==&mid=2653200029&idx=1&sn=51ecebafb9ff77baf3de71bdc4f67b78"/>漫画：什么是归并排序？</p>
 *
 * @author: zhangcx
 * @date: 2019/10/8 9:40
 */
public class MergeSort {
    public static void mergeSort(int[] arr, int start, int end) {
        if (start < end) {
            // 折半成两个小集合，分别进行递归
            int mid = (start + end) / 2;
            mergeSort(arr, start, mid);
            mergeSort(arr, mid + 1, end);
            // 把两个有序小集合，归并成一个大集合
            merge(arr, start, mid, end);
        }
    }

    private static void merge(int[] arr, int start, int mid, int end) {
        // 开辟额外大集合，设置指针
        int[] tempArray = new int[end - start + 1];
        // p1 左侧小集合的游标，p2 右侧小集合的游标，p 额外大集合的游标
        int p1 = start, p2 = mid + 1, p = 0;
        // 比较两个小集合的元素，依次放入大集合
        while (p1 <= mid && p2 <= end) {
            if (arr[p1] <= arr[p2]) {
                tempArray[p++] = arr[p1++];
            } else {
                tempArray[p++] = arr[p2++];
            }
        }
        // 左侧小集合还有剩余，依次放入大集合尾部
        while (p1 <= mid) {
            tempArray[p++] = arr[p1++];
        }
        // 右侧小集合还有剩余，依次放入大集合尾部
        while (p2 <= end) {
            tempArray[p++] = arr[p2++];
        }
        // 把大集合的元素复制回原数组
        for (int i = 0; i < tempArray.length; i++) {
            arr[start + i] = tempArray[i];
        }
    }

    public static void main(String[] args) {
        int[] arr = {11, 5, 8, 0, 6, 75, 3, 103, 9, 2, 1, 7, 2, 4};
        System.out.println(Arrays.toString(arr));
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
