package com.lzycug.singleton;

/**
 * @author: lzyCug
 * @date: 2021/3/2 14:50
 * @description: 懒汉式线程安全单例, 内部类实现
 */
public class Singleton6 {
    public static Singleton6 getInstance() {
        return SingletonFactory.singleton;
    }

    private static class SingletonFactory {
        private static final Singleton6 singleton = new Singleton6();
    }

    private Singleton6() {
    }
}
