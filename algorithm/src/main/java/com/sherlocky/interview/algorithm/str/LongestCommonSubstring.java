package com.sherlocky.interview.algorithm.str;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 最长公共子串：子串要求在原字符串中是连续的
 * @author: zhangcx
 * @date: 2019/11/13 10:54
 * @since:
 */
public class LongestCommonSubstring {
    public static void main(String[] args) {
        LongestCommonSubstring s = new LongestCommonSubstring();
        String s1 = "asdfas", s2 = "werasdfaswer";
        // 6
        System.out.println(s.lcsLength(s1, s2));
        s1 = "abcbdab";
        s2 = "bdcaba";
        // 2
        System.out.println(s.lcsLength(s1, s2));
    }

    /**
     * 计算两个字符串的最大公共子串（Longest Common Substring）的长度，字符不区分大小写
     */
    public int lcsLength(String s1, String s2) {
        if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
            return 0;
        }
        // 定义一个二维数组，记录两个字符串s1和s2的LCS长度（长度分别比原字符串长1），即所谓的动态规划表。
        int[][] matrix = new int[s1.length() + 1][s2.length() + 1];
        // 初始化数组的所有元素都为0 （注意for语句里是 i <= ）
        for (int i = 0; i <= s1.length(); i++) {
            matrix[i][0] = 0;
        }
        for (int j = 0; j <= s2.length(); j++) {
            matrix[0][j] = 0;
        }
        int longest = 0;
        /**
         * 问题 C[i, j] 可以分解为：
         * 1. if x[i] == y[i]，则: C[i, j] = C[i-1, j-1] + 1
         * 2. if x[i] ≠ y[i]，则: C[i, j] = 0
         */
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                // 如果X(i-1) == Y(j-1)，那么最长子串为 C[i-1][j-1] + 1
                if (Objects.equals(s1.charAt(i - 1), s2.charAt(j - 1))) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    longest = Math.max(longest, matrix[i][j]);
                } else {
                    // X(i-1) ≠ Y(j-1) ==> 0
                    matrix[i][j] = 0;
                }
            }
        }
        return longest;
    }
}
