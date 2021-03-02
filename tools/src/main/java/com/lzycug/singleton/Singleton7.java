package com.lzycug.singleton;

import org.springframework.util.ObjectUtils;

/**
 * @author: lzyCug
 * @date: 2021/3/2 15:27
 * @description: 使用ThreadLocal实现单例模式
 */
public class Singleton7 {
    private static final ThreadLocal<Singleton7> threadLocal = new ThreadLocal<>();
    private static Singleton7 singleton;

    private Singleton7() {
    }

    public static Singleton7 getInstance() {
        if (ObjectUtils.isEmpty(threadLocal.get())) {
            synchronized (Singleton7.class) {
                if (ObjectUtils.isEmpty(singleton)) {
                    singleton = new Singleton7();
                }
            }
            threadLocal.set(singleton);
        }
        return threadLocal.get();
    }
}