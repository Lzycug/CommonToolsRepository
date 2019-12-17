/*
 * Copyright (c) Lizhiyang  xi'an China. 2019-2019. All rights reserved.
 */

package com.lzycug.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * JedisPool工具类
 *
 * @author lzycug
 * @since 2019-12-17
 */
public class JedisPoolUtils {
    private static JedisPool jedisPool;

    static {
        InputStream stream = JedisPoolUtils.class.getClassLoader().getResourceAsStream("JedisPool.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.getMessage();
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置可用最大连接数
        jedisPoolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("MAX_TOTAL")));
        // 设置最大空闲连接数
        jedisPoolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("MAX_IDLE")));
        // 设置最长等待时间
        jedisPoolConfig.setMaxWaitMillis(Long.parseLong(properties.getProperty("MAX_WAIT")));
        // 在获取redis连接时，自动检测连接是否有效
        jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("TEST_ON_BORROW")));
        jedisPool = new JedisPool(jedisPoolConfig, properties.getProperty("ADDR"),
            Integer.parseInt(properties.getProperty("PORT")));
    }

    /**
     * 获取Jedis
     *
     * @return Jedis对象
     */
    public synchronized static Optional<Jedis> getJedis() {
        if (!ObjectUtils.isEmpty(jedisPool)) {
            return Optional.ofNullable(jedisPool.getResource());
        } else {
            return Optional.empty();
        }
    }

    /**
     * 资源释放
     *
     * @param jedis
     */
    public synchronized static void releaseConn(Jedis jedis) {
        if (!ObjectUtils.isEmpty(jedisPool)) {
            jedisPool.returnResource(jedis);
        }
    }
}
