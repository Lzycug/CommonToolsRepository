package com.lzycug.singleton;

/**
 * @author lzyCug
 * @date 2021/3/2 11:27
 * @description: 饿汉式单例
 */
public class Singleton1 {
    private static Singleton1 singleton = new Singleton1();

    public static Singleton1 getInstance() {
        return singleton;
    }

    private Singleton1() {
    }
}
