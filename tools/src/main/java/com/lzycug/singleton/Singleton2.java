package com.lzycug.singleton;

import org.springframework.util.ObjectUtils;

/**
 * @author lzyCug
 * @date 2021/3/2 14:50
 * @description: 懒汉式线程不安全单例
 */
public class Singleton2 {
    private static Singleton2 singleton;

    public static Singleton2 getInstance() {
        if (ObjectUtils.isEmpty(singleton)) {
            singleton = new Singleton2();
        }
        return singleton;
    }

    private Singleton2() {
    }
}
