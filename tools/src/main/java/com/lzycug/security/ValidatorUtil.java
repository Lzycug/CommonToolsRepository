package com.lzycug.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 验证指定bean对象是否合法
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class ValidatorUtil {
    private static final Logger LOGGER = LogManager.getLogger(ValidatorUtil.class);

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtil() {
    }

    /**
     * 验证指定对象
     *
     * @param <T> 泛型参数类型
     * @param object 待验证对象
     * @return 验证错误信息
     */
    public static <T> Map<String, String> validate(T object) {
        Set<ConstraintViolation<T>> verificationResults = validator.validate(object);
        if (verificationResults == null || verificationResults.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> errorMap = new HashMap<>(verificationResults.size());
        verificationResults.forEach(violation -> {
            if (violation != null && violation.getPropertyPath() != null) {
                errorMap.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage());
            }
        });
        return errorMap;
    }

    /**
     * 判断指定对象是否合法
     *
     * @param <T> 泛型参数类型
     * @param object 待验证对象
     * @return 是否合法
     */
    public static <T> boolean isLegal(T object) {
        if (object == null) {
            LOGGER.warn("input param is empty!");
            return false;
        }
        Set<ConstraintViolation<T>> verificationResults = validator.validate(object);
        if (verificationResults == null || verificationResults.isEmpty()) {
            return true;
        }
        verificationResults.forEach(violation -> {
            if (violation != null && violation.getPropertyPath() != null) {
                LOGGER.error("class:{} field:{} is illegal:{}.", object.getClass().getSimpleName(),
                    violation.getPropertyPath().toString(), violation.getMessage());
            }
        });
        return false;
    }
}
