package com.lzycug.leetcode.questionBank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * description：
 * author：lzyCug
 * date: 2020/11/3 17:54
 */
@Slf4j
public class MountainArray {
    public static void main(String[] args) {
        int[] array = {3,5,6,9,5,6,1};
        boolean result = validMountainArray(array);
        System.out.println(result);
    }

    private static boolean validMountainArray(int[] array) {
        int max = array[0];
        int maxInx = 0;
        if (ObjectUtils.isEmpty(array) || array.length <= 2) {
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
        for (int i = maxInx; i < array.length - 2; i++) {
            if (array[i] <= array[i + 1]) {
                behindFlag = false;
                break;
            }
        }
        return frontFlag && behindFlag;
    }
}
