
package com.lzycug.leetcode.questionBank;

/**
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * 示例 1:
 * 输入: 123
 * 输出: 321
 *  示例 2:
 * 输入: -123
 * 输出: -321
 * 示例 3:
 * 输入: 120
 * 输出: 21
 * 注意:
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
 *
 * @author lWX716128
 * @since 2019-12-10
 */
class IntegerInversion {
    public static int reverse(int x) {
        int last = x;
        int result = 0;
        boolean negativeFlag = false;
        int range1 = 214748364;
        if (last < 0) {
            last = -last;
            negativeFlag = true;
        }
        int range2 = negativeFlag ? 8 : 7;   // 负数为8, 正数为7

        while (last > 0) {
            int temp = last % 10;
            last = last / 10;
            if (result > range1 || (result == range1 && temp > range2)) {
                return 0;
            }
            result = 10 * result + temp;
        }
        result = negativeFlag ? -result : result;
        return result;
    }

    public static void main(String[] args) {
        int reverse = reverse(123);
    }
}
