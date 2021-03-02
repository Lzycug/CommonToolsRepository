package com.lzycug.singleton;

import org.springframework.util.ObjectUtils;

/**
 * @author lzyCug
 * @date 2021/3/2 14:50
 * @description: 懒汉式线程安全单例
 */
public class Singleton4 {
    private static Singleton4 singleton;

    public static Singleton4 getInstance() {
        if (ObjectUtils.isEmpty(singleton)) {
            synchronized (Singleton4.class) {
                if (ObjectUtils.isEmpty(singleton)) {
                    singleton = new Singleton4();
                }
            }
        }
        return singleton;
    }

    private Singleton4() {
    }
}
