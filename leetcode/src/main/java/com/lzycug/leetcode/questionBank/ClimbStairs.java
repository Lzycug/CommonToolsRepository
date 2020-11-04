package com.lzycug.leetcode.questionBank;

/**
 * description：假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 注意：给定 n 是一个正整数。
 * <p>
 * author：lzyCug
 * date: 2020/11/4 10:10
 */
public class ClimbStairs {
    public static void main(String[] args) {
        System.out.println(climbStairsMemo(45));
    }

    private static int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        return climbStairs(n - 1) + climbStairs(n - 2);
    }

    private static int climbStairsMemo(int n, int[] memo) {
        if (memo[n] > 0) {
            return memo[n];
        }
        if (n == 1) {
            memo[n] = 1;
        } else if (n == 2) {
            memo[n] = 2;
        } else {
            memo[n] = climbStairsMemo(n - 1, memo) + climbStairsMemo(n - 2, memo);
        }
        return memo[n];
    }

    private static int climbStairsMemo(int n) {
        return climbStairsMemo(n, new int[n + 1]);
    }
}
