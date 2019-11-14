package com.sherlocky.interview.algorithm.str;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 最长公共子序列：子序列只需保持相对顺序一致，并不要求连续
 * <p>
 * 主题思想使用动态规划
 *
 * 参考：https://www.kancloud.cn/digest/pieces-algorithm/163624
 *
 * @author: zhangcx
 * @date: 2019/11/13 10:54
 * @since:
 */
public class LongestCommonSubsequence {
    public static void main(String[] args) {
        LongestCommonSubsequence s = new LongestCommonSubsequence();
        String s1 = "abcbdab", s2 = "bdcaba";
        // 4
        System.out.println(s.lcsLength(s1, s2));
        // bcba
        System.out.println(s.lcsContent_(s1, s2));
        // [bcab, bdab, bcba]
        System.out.println(s.lcsContents(s1, s2));
    }

    /**
     * 如果只求最长子序列长度 {@link #lcsLengthMatrix}
     * @param s1
     * @param s2
     * @return
     */
    public int lcsLength(String s1, String s2) {
        if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
            return 0;
        }
        // 从二维数组中获取最长子序列长度（最靠后的元素）
        int[][] matrix = lcsLengthMatrix(s1, s2);
        return matrix[s1.length()][s2.length()];
    }

    /**
     * 如果只求最长子序列长度
     * <p>
     * 使用【动态规划】
     * <p>
     * 【时间复杂度】是O(mn)，这比简单的递归实现要快多了。
     * 【空间复杂度】是O(mn)，因为使用了一个动态规划表。
     * @return
     */
    public int[][] lcsLengthMatrix(String s1, String s2) {
        if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
            return null;
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
        /**
         * 问题 C[i, j] 可以分解为：
         * 1. if x[i] == y[i]，则: C[i, j] = C[i-1, j-1] + 1
         * 2. if x[i] ≠ y[i]，则: C[i, j] = max(C[i, j-1], C[i-1, j])
         */
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                // 如果X(i-1) == Y(j-1)，那么最长子序列为 C[i-1][j-1] + 1
                if (Objects.equals(s1.charAt(i - 1), s2.charAt(j - 1))) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    continue;
                }
                matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
            }
        }
        return matrix;
    }

    /**
     * 该函数找出所有的LCS的序列，并将其存在 Set 中
     * <p>
     * 输出的顺序是先向左寻找，再向上寻找 ←↑
     * <p>
     * 两个字符串对应的最长公共子序列不一定唯一，输出所有的LCS内容的基本思想是：
     * <ul>
     *  <li>1.如果格子matrix[i,j]对应的X[i-1] == Y[j-1]，则把这个字符放入LCS中，并跳入matrix[i-1,j-1]中继续进行判断；</li>
     *  <li>2.如果格子matrix[i,j]对应的X[i-1] ≠ Y[j-1]，则比较matrix[i-1,j]和matrix[i,j-1]的值，跳入值较大的格子继续进行判断；</li>
     *  <li>3.直到i或j小于等于0为止，倒序输出LCS。</li>
     *  <li>4.针对第2条，如果出现matrix[i-1,j]等于matrix[i,j-1]的情况，说明最长公共子序列有多个，故两边都要进行回溯（可以递归）。</li>
     * </ul>
     * </p>
     * @param s1
     * @param s2
     * @return
     */
    public Set<String> lcsContents(String s1, String s2) {
        int[][] matrix = lcsLengthMatrix(s1, s2);
        Set<String> lcsSet = new HashSet<>();
        String lcsStr = "";
        allLCS(s1, s2, s1.length(), s2.length(), matrix, lcsStr, lcsSet);
        return lcsSet;
    }

    private void allLCS(String s1, String s2, int i, int j, int[][] matrix, String lcsStr, Set<String> lcsSet) {
        while (i > 0 && j > 0) {
            if (Objects.equals(s1.charAt(i - 1), s2.charAt(j - 1))) {
                // 逆序存放
                lcsStr = s1.charAt(i - 1) + lcsStr;
                --i;
                --j;
            } else {
                // 向左
                if (matrix[i][j - 1] < matrix[i - 1][j]) {
                    --i;
                } else if (matrix[i][j - 1] > matrix[i - 1][j]) {
                    // 向上
                    --j;
                } else {
                    // 此时向左，向上均为LCS元素
                    allLCS(s1, s2, i - 1, j, matrix, lcsStr, lcsSet);
                    allLCS(s1, s2, i, j - 1, matrix, lcsStr, lcsSet);
                    return;
                }
            }
        }
        lcsSet.add(lcsStr);
    }

    /**
     * 输出一个最长子序列的内容
     * 和求最长子序列长度相比，只需要多一个二维数组记录在遍历中所选择的子问题的最优解即可。
     */
    public String lcsContent_(String s1, String s2) {
        if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
            return null;
        }
        // 定义一个二维数组，记录两个字符串s1和s2的LCS长度（长度分别比原字符串长1），即所谓的动态规划表。
        int[][] matrix = new int[s1.length() + 1][s2.length() + 1];
        int[][] matrixContent = new int[s1.length() + 1][s2.length() + 1];
        // 初始化数组的所有元素都为0 （注意for语句里是 i <= ）
        for (int i = 0; i <= s1.length(); i++) {
            matrix[i][0] = 0;
        }
        for (int j = 0; j <= s2.length(); j++) {
            matrix[0][j] = 0;
        }
        /**
         * 问题 C[i, j] 可以分解为：
         * 1. if x[i] == y[i]，则: C[i, j] = C[i-1, j-1] + 1
         * 2. if x[i] ≠ y[i]，则: C[i, j] = max(C[i, j-1], C[i-1, j])
         */
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (Objects.equals(s1.charAt(i - 1), s2.charAt(j - 1))) {
                    // 如果X(i-1) == Y(j-1)，那么最长子序列为 C[i-1][j-1] + 1
                    // 此时将 matrixContent[i][j] = 1 表明 s1[i-1] 是子问题LCS的一个元素
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;

                    matrixContent[i][j] = 1;
                    continue;
                }
                if (matrix[i][j - 1] > matrix[i - 1][j]) {
                    matrix[i][j] = matrix[i][j - 1];

                    matrixContent[i][j] = 2;
                    continue;
                }
                matrix[i][j] = matrix[i - 1][j];
                matrixContent[i][j] = 3;
            }
        }
        return oneLCS(matrixContent, s1, s1.length(), s2.length());
    }

    /**
     * 输出一个LCS的序列
     * 这里输出的顺序是先向上寻找，再向左寻找
     */
    private String oneLCS(int[][] matrixContent, String s1, int i, int j) {
        if (i == 0 || j == 0) {
            return "";
        }
        if (matrixContent[i][j] == 1) {
            return oneLCS(matrixContent, s1, i - 1, j - 1) + s1.charAt(i - 1);
        }
        if (matrixContent[i][j] == 2) {
            return oneLCS(matrixContent, s1, i, j - 1);
        }
        return oneLCS(matrixContent, s1, i - 1, j);
    }
}
