
package com.lzycug.leetcode.questionBank;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 *
 * @author lWX716128
 * @since 2019-12-06
 */
public class TwoNumSum {
    public static void main(String[] args) {
        int[] nums = {-2, 7, -11, 15};
        int target = -4;
        int[] subscript = twoSum(nums, target);
        System.out.println("[" + subscript[0] + ", " + subscript[1] + "]");
    }

    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标
     *
     * @param nums 给定的数组
     * @param target 目标值
     * @return 数组下标的数组
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] array = new int[2];
        if (nums.length < 2) {
            return array;
        } else {
            for (int i = 0; i < nums.length; i++) {
                int diff = target - nums[i];
                boolean isSum = false;
                for (int j = 0; j < nums.length; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (diff == nums[j]) {
                        array[0] = i;
                        array[1] = j;
                        isSum = true;
                        break;
                    }
                }
                if (isSum) {
                    break;
                }
            }
            return array;
        }
    }
}
