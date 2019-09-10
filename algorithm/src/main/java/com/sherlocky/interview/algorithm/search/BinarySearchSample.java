package com.sherlocky.interview.algorithm.search;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二分查找 示例
 * <p>在一段有序的数组里面查找某一个数字时可以使用二分查找算法，来实现<code>O(lg n)</code>时间复杂度的查找</p>
 * jdk 原生实现了数组的二分查找：Arrays.binarySearch()
 * @author: zhangcx
 * @date: 2019/5/27 9:01
 */
public class BinarySearchSample {

    /**
     * 对于普通有序数组的二分查找实现（元素不重复）
     * @param datas
     * @param target
     * @return
     */
    public static int binarySearch(int[] datas, int target) {
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
            // mid = start + (end - start) / 2
            int midVal = datas[mid];
            if (midVal == target) {
                return mid;
            }
            if (midVal > target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 于普通有序数组的二分查找实现（元素可能重复）
     * @param datas
     * @param target
     * @return
     */
    public static List<Integer> binarySearch4DuplicateElement(int[] datas, int target) {
        if (datas.length == 0) {
            return new ArrayList<>();
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
            if (midVal == target) {
                List<Integer> result = new ArrayList<>();
                result.add(mid);
                int leftIdx = mid, rightIdx = mid;
                // 向前需找可能的重复元素
                while (datas[--leftIdx] == target) {
                    result.add(leftIdx);
                }
                // 向前需找可能的重复元素
                while (datas[++rightIdx] == target) {
                    result.add(rightIdx);
                }
                // 向后需找可能的重复元素
                return result;
            }
            if (midVal > target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 对于旋转有序数组的二分查找实现（元素不重复）
     * <p>旋转后的数组不再满足整体有序的特性了，因此不能直接的套用二分查找算法，但是旋转之后的数组是分段有序的。</p>
     * <p>可以分别在两段数组上进行二分查找操作，因此在每次循环中需要判断哪一段是递增的。</p>
     * 例如： [4, 5, 6, 7, 0, 1, 2]
     *
     * <p><a href="https://mp.weixin.qq.com/s?__biz=MzIxMjE5MTE1Nw==&mid=2653198327&idx=1&sn=74187f72a03db8fcad3c234d61c7bad7&chksm=8c99e52dbbee6c3b1e35ea8b2a8d57fa9fa72c098fa91e9f76a3228596922ef062fbf6f3e103&scene=0&xtrack=1&key=98ed5c14c3ff9f33c103b226b378c86ce179a8d7bc9ee077ca59e8e1460a827dc755298c3931aa789ba56fce37e18a6dbb79bea2c45a7d9cc3333775589db02201a0e54aa3186a01dccfd4aa8922e45c&ascene=1&uin=NDc3NjExMDE1&devicetype=Windows+7&version=62060739&lang=zh_CN&pass_ticket=Yg8hweHVbfhxPXs8uifwgFbff7pVaEu%2BW4iR9Nwbn%2BGFhhPdUiqrkK26q4bNkJ%2FZ">漫画：“旋转数组”中的二分查找</a></p>
     *
     * @param rotatedDatas
     * @param target
     * @return
     */
    public static int binarySearch4RotatedSortedArray(int[] rotatedDatas, int target) {
        if (rotatedDatas.length == 0) {
            return -1;
        }
        // 查找范围起点
        int start = 0;
        // 查找范围终点
        int end = rotatedDatas.length - 1;

        // 迭代二分查找
        while (start <= end) {
            // 查找范围中位数【通过中位数下标，虚拟将数组一分为两段，则必有一段(可能两段)是有序的】
            int mid = (start + end) >>> 1; // 无符号右移一位（除以2）
            if (rotatedDatas[mid] == target) {
                return mid;
            }
            if (rotatedDatas[start] <= rotatedDatas[mid]) { // 左侧一段 有序
                if (rotatedDatas[start] <= target && target < rotatedDatas[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else { // 后一段 有序
                if (rotatedDatas[mid] < target && target <= rotatedDatas[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * 如果旋转数组中存在重复的数字的话，只通过类似binarySearch4RotatedSortedArray()方法中的判断是不准确的，还需要多判断一种情况。
     * <p>比如考虑旋转数组：{1,1,0,1,1,1,1}，第一次循环时，datas[start] = datas[mid] = datas[end] = 1</p>
     * <P>这样我们就没有办法判断出 start --- mid 与 mid --- end 段，哪一段是有序的了，这时可以只能采用线性遍历 start---end 段得出结论了。</p>
     * @param rotatedDatas
     * @param target
     * @return
     */
    public static boolean binarySearch4RotatedSortedArrayWithDuplicateElement(int[] rotatedDatas, int target) {
        if (rotatedDatas.length == 0) {
            return false;
        }
        // 查找范围起点
        int start = 0;
        // 查找范围终点
        int end = rotatedDatas.length - 1;

        // 迭代二分查找
        while (start <= end) {
            // 查找范围中位数【通过中位数下标，虚拟将数组一分为两段，则必有一段(可能两段)是有序的】
            int mid = (start + end) >>> 1; // 无符号右移一位（除以2）
            // 但是当元素存在重复时，极限情况我们无法通过 和中位数下标对应的数字 进行比较区分哪一段是有序的
            if (rotatedDatas[start] == rotatedDatas[mid] && rotatedDatas[mid] == rotatedDatas[end]) {
                return ArrayUtils.contains(rotatedDatas, target);
            }

            if (rotatedDatas[mid] == target) {
                return true;
            }
            if (rotatedDatas[start] <= rotatedDatas[mid]) { // 左侧一段 有序
                if (rotatedDatas[start] <= target && target < rotatedDatas[mid]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            } else { // 后一段 有序
                if (rotatedDatas[mid] < target && target <= rotatedDatas[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] datas = new int[] { 12, 35, 35, 35, 35, 35, 35, 39, 42, 59, 77, 90, 102, 159, 203 };
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
        System.out.println("## 重复元素的情况：");
        start = System.nanoTime();
        System.out.println(binarySearch4DuplicateElement(datas, 35));
        end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");

        int[] rotatedDatas = new int[]{4, 5, 6, 7, 0, 1, 2};
        System.out.println("## 旋转有序数组的情况：");
        start = System.nanoTime();
        System.out.println(binarySearch4RotatedSortedArray(rotatedDatas, 0));
        end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");
        rotatedDatas = new int[]{1, 1, 0, 1, 1, 1, 1};
        System.out.println("## 旋转有序数组重复的情况1：");
        start = System.nanoTime();
        System.out.println(binarySearch4RotatedSortedArrayWithDuplicateElement(rotatedDatas, 0));
        end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");
        rotatedDatas = new int[]{2, 5, 6, 0, 0, 1, 2};
        System.out.println("## 旋转有序数组重复的情况2：");
        start = System.nanoTime();
        System.out.println(binarySearch4RotatedSortedArrayWithDuplicateElement(rotatedDatas, 0));
        end = System.nanoTime();
        System.out.println("#### 用时：" + (end -start) + " 纳秒");

        // System.arraycopy 简单测试
        System.out.println("#####################System.arraycopy######################");
        int[] array1 = {1,2,3,4,5};
        int[] array2 = {11,12,13,14,15,16};
        System.arraycopy(array1, 1, array2, 2, 3);
        System.out.println(JSON.toJSONString(array2));
    }
}
