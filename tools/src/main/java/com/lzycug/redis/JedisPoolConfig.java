
package com.lzycug.redis;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置信息类
 *
 * @author lzycug
 * @since 2019-12-17
 */
public class JedisPoolConfig {

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String port;

    /**
     * 构造方法
     */
    private JedisPoolConfig() {
        init();
    }

    /**
     * 初始化配置信息
     */
    private void init() {
        InputStream stream = JedisPoolConfig.class.getClassLoader().getResourceAsStream("JedisPool.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.getMessage();
        }
        JedisPoolConfig instance = JedisPoolConfig.getInstance();
    }

    /**
     * 配置信息内部类
     *
     * @author lzycug
     * @since 2019-12-17
     */
    private static final class UploadDownSingleton {
        private static final JedisPoolConfig INSTATCE = new JedisPoolConfig();
    }

    /**
     * 获取单例
     *
     * @return 配置类单例对象
     */
    public static JedisPoolConfig getInstance() {
        System.out.println("111");
        return UploadDownSingleton.INSTATCE;
    }
}
