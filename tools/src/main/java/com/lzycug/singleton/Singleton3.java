package com.lzycug.singleton;

import org.springframework.util.ObjectUtils;

/**
 * @author lzyCug
 * @date 2021/3/2 14:50
 * @description: 懒汉式线程安全单例
 */
public class Singleton3 {
    private static Singleton3 singleton;

    public static synchronized Singleton3 getInstance() {
        if (ObjectUtils.isEmpty(singleton)) {
            singleton = new Singleton3();
        }
        return singleton;
    }

    private Singleton3() {
    }
}
