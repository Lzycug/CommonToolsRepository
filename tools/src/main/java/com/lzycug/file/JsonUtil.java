package com.lzycug.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lzycug.security.ValidatorUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JSON格式操作工具类
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class JsonUtil {
    private static final Logger LOGGER = LogManager.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    /**
     * 对象转JSON字符串
     * 调用点判断是否是Optional.empty()，推荐使用ifPresent()，if或者filter中使用isPresent也可以
     *
     * @param obj 待转换对象
     * @param <T> 对象类型
     * @return 转换后JSON字符串
     */
    public static <T> Optional<String> beanToJson(T obj) {
        if (obj == null) {
            LOGGER.error("input object is empty.");
            return Optional.empty();
        }

        String jsonString = null;
        try {
            jsonString = JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
        } catch (JSONException e) {
            LOGGER.error("illegal obj:{}.", obj.getClass().getSimpleName());
        }
        return Optional.ofNullable(jsonString);
    }

    /**
     * JSON字符串转对象
     * 调用点判断是否是Optional.empty()，推荐使用ifPresent()，if或者filter中使用isPresent也可以
     *
     * @param jsonStr 待转换JSON字符串
     * @param objClass 转换对象
     * @param <T> 对象类型
     * @return 转换后对象
     */
    public static <T> Optional<T> jsonToBean(String jsonStr, Class<T> objClass) {
        if (StringUtils.isBlank(jsonStr) || Objects.isNull(objClass)) {
            LOGGER.error("input param is empty.");
            return Optional.empty();
        }

        T bean = null;
        try {
            bean = JSON.parseObject(jsonStr, objClass);
        } catch (JSONException e) {
            LOGGER.error("illegal json, failed to obj:{}.", objClass.getClass().getSimpleName());
        }
        if (!ValidatorUtil.isLegal(bean)) {
            bean = null;
        }
        return Optional.ofNullable(bean);
    }

    /**
     * JSON字符串转对象列表
     * 调用点判断是否是Optional.empty()，推荐使用ifPresent()，if或者filter中使用isPresent也可以
     *
     * @param jsonStr 待转换JSON字符串
     * @param objClass 转换对象
     * @param <T> 对象类型
     * @return 转换后对象列表
     */
    public static <T> Optional<List<T>> jsonToBeans(String jsonStr, Class<T> objClass) {
        if (StringUtils.isBlank(jsonStr) || Objects.isNull(objClass)) {
            LOGGER.error("input param is empty.");
            return Optional.empty();
        }

        List<T> beanList = null;
        try {
            beanList = JSON.parseArray(jsonStr, objClass);
        } catch (JSONException e) {
            LOGGER.error("illegal json, failed to obj:{}.", objClass.getClass().getSimpleName());
        }
        beanList.removeIf(bean -> !ValidatorUtil.isLegal(bean));
        return Optional.ofNullable(beanList);
    }

    /**
     * JSON字符串转JSONObject
     * 调用点判断是否是Optional.empty()，推荐使用ifPresent()，if或者filter中使用isPresent也可以
     *
     * @param jsonStr 待转换JSON字符串
     * @return 转换后JSONObject
     */
    public static Optional<JSONObject> jsonToJsonObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            LOGGER.error("input jsonStr is empty.");
            return Optional.empty();
        }

        JSONObject obj = null;
        try {
            obj = JSON.parseObject(jsonStr);
        } catch (JSONException e) {
            LOGGER.error("illegal input json.");
        }
        return Optional.ofNullable(obj);
    }
}
