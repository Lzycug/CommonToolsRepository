package com.lzycug.singleton;

import org.springframework.util.ObjectUtils;

/**
 * @author lzyCug
 * @date 2021/3/2 14:50
 * @description: 懒汉式线程安全单例
 */
public class Singleton5 {
    private static volatile Singleton5 singleton;

    public static Singleton5 getInstance() {
        if (ObjectUtils.isEmpty(singleton)) {
            synchronized (Singleton5.class) {
                if (ObjectUtils.isEmpty(singleton)) {
                    singleton = new Singleton5();
                }
            }
        }
        return singleton;
    }

    private Singleton5() {
    }
}
