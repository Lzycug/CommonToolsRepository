package com.lzycug.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class XmlConfigUtil {
    private static final Logger logger = LogManager.getLogger(XmlConfigUtil.class);

    private static Properties properties = new Properties();

    // 静态代码块预先加载配置文件
    static {
        InputStream isConfig = null;
        try {
            // 获类加载器
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            // 加载配置文件进流
            isConfig = cl.getResourceAsStream("config.properties");
            properties.load(isConfig);
        } catch (Exception e) {
            logger.error("ConfigUtil类的静态代码块出错", e);
        } finally {
            try {
                if (null != isConfig) {
                    isConfig.close();
                }
            } catch (IOException e) {
                logger.error("释放批量配置资源异常!", e);
            }
        }
    }

    // 通过Property name 获取Property
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
