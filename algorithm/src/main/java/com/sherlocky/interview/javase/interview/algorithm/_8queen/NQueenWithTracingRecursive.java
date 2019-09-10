package com.sherlocky.interview.javase.interview.algorithm._8queen;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * 递归回溯法解决N皇后问题
 * <p>n皇后问题
 *
 * 要在n*n的国际象棋棋盘中放n个皇后，使任意两个皇后都不能互相吃掉。
 * 规则：皇后能吃掉同一行、同一列、同一对角线的任意棋子。求所有的解。
 * n=8是就是著名的八皇后问题了。
 * </p>
 * <p>
 * 回溯法的基本做法是搜索，或是一种组织得井井有条的，能避免不必要搜索的穷举式搜索法。这种方法适用于解一些组合数相当大的问题。
 *
 * 回溯法在问题的解空间树中，按深度优先策略，从根结点出发搜索解空间树。算法搜索至解空间树的任意一点时，先判断该结点是否包含问题的解。
 * 如果肯定不包含，则跳过对该结点为根的子树的搜索，逐层向其祖先结点回溯；否则，进入该子树，继续按深度优先策略搜索。
 *
 * 回溯法指导思想——走不通，就掉头。设计过程：确定问题的解空间；确定结点的扩展规则；搜索。
 * </p>
 *
 * @author: zhangcx
 * @date: 2019/1/11 9:19
 */
public class NQueenWithTracingRecursive {
    private static int SIZE = 0;//皇后的个数
    private static int count = 0;//记录摆放的组合数

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入皇后的个数：");
        SIZE = input.nextInt();
        input.close();
        LinkedList<Location> list = new LinkedList<Location>();
        // 统计用时
        long begintime = System.currentTimeMillis();

        nQueen(list, 0);  //从棋盘的第0行第0列开始

        long endtime = System.currentTimeMillis();
        long costTime = endtime - begintime;

        System.out.println(SIZE + "皇后共有：" + count + " 种摆放方式，算法耗时 " + costTime + " 毫秒。");
    }

    private static class Location {
        /** 对应棋盘的行 */
        int x;
        /** 对应棋盘的列 */
        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    /**
     * 主要函数，用回溯法。
     */
    private static void nQueen(LinkedList<Location> list, int x) {
        if (list.size() == SIZE) {  //当list元素个数为SIZE时，表示SIZE个皇后都摆放完毕，打印后即可退出函数。
            printLocation(list);  //打印皇后摆放方式
            return;
        }
        // 从第0列开始尝试摆放
        for (int i = 0; i < SIZE; i++) {
            Location loc = new Location(x, i);
            if (isLegalLoc(list, loc)) {//当前坐标如果符合皇后的规则要求  把坐标加到list中来
                list.add(loc);
                nQueen(list, x + 1);  //开始摆放x+1行的皇后，同样从第0列开始摆放
                list.pollLast();  //每次摆放完一个皇后之后，都要将其撤回，再试探其它的摆法。
            }
        }
    }


    /**
     * 判断位置为loc的皇后是否合法
     */
    private static boolean isLegalLoc(LinkedList<Location> list, Location loc) {
        for (Location each : list) {
            // 判断是否在同一行或同一列
            if (loc.x == each.x || loc.y == each.y) {
                return false;
            }
            // 判断是否在同斜线上
            if (Math.abs(loc.x - each.x) == Math.abs(loc.y - each.y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印皇后摆放方式
     *
     * @param list
     */
    private static void printLocation(LinkedList<Location> list) {
        String[][] show = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                show[i][j] = "0";
            }
        }
        for (Location each : list) {
            show[each.x][each.y] = "1";
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(show[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("________________");
        // System.out.println();
        count++;
    }

}
