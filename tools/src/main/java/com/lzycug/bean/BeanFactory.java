package com.lzycug.bean;

import lombok.NonNull;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * 类创建工厂
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class BeanFactory {
    private static final Logger LOG = LogManager.getLogger(BeanFactory.class);

    private BeanFactory() {
    }

    /**
     * 根据类名创建实例
     *
     * @param type 类型
     * @param className 类名
     * @param <T> 泛型
     * @return 类实例
     */
    public static <T> Optional<T> newInstance(@NonNull Class<T> type, @NonNull String className) {
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            instance = constructor == null ? null : constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOG.error(ExceptionUtils.getStackTrace(e));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOG.error("Illegal class:{}!", className);
        }
        if (type.isInstance(instance)) {
            return Optional.ofNullable(type.cast(instance));
        }
        return Optional.empty();
    }
}
