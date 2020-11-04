package com.lzycug.leetcode.questionBank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * description：给定一个整数数组 A，如果它是有效的山脉数组就返回 true，否则返回 false。
                如果 A 满足下述条件，那么它是一个山脉数组：
                A.length >= 3
                 在 0 < i < A.length - 1 条件下，存在 i 使得：
                 A[0] < A[1] < ... A[i-1] < A[i]
                 A[i] > A[i+1] > ... > A[A.length - 1]
 * author：lzyCug
 * date: 2020/11/3 17:54
 */
@Slf4j
public class MountainArray {
    public static void main(String[] args) {
        int[] array = {1, 7, 9, 5, 4, 1, 2};
        long beginTime = System.currentTimeMillis();
        boolean result = validMountainArray(array);
        System.out.println(result + "execute time:" + (System.currentTimeMillis() - beginTime));
    }

    private static boolean validMountainArray(int[] array) {
        int max = array[0];
        int maxInx = 0;
        if (ObjectUtils.isEmpty(array) || array.length < 3) {
            log.info("array isn't mountainArray, the array length is {}", array.length);
            return false;
        }
        for (int i = 1; i < array.length; i++) {
            if (array[i] >= max) {
                max = array[i];
                maxInx = i;
            }
        }
        if (maxInx == 0 || maxInx == array.length - 1) {
            log.info("array isn't mountainArray, the maxValue index is {}", maxInx);
            return false;
        }
        boolean frontFlag = true;
        for (int i = 0; i < maxInx; i++) {
            if (array[i] >= array[i + 1]) {
                frontFlag = false;
                break;
            }
        }

        boolean behindFlag = true;
        for (int i = maxInx; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                behindFlag = false;
                break;
            }
        }
        return frontFlag && behindFlag;
    }

    private static boolean validMountainArrayFind(int[] A) {
        int len = A.length;
        int left = 0;
        int right = len - 1;
        //从左边往右边找，一直找到山峰为止
        while (left + 1 < len && A[left] < A[left + 1])
            left++;
        //从右边往左边找，一直找到山峰为止
        while (right > 0 && A[right - 1] > A[right])
            right--;
        //判断从左边和从右边找的山峰是不是同一个
        return left > 0 && right < len - 1 && left == right;
    }
}
