package com.lzycug.file;

import com.lzycug.security.ValidatorUtil;

import lombok.NonNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * yaml文件读取操作工具类
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class YamlReader {
    private static final Logger LOGGER = LogManager.getLogger(YamlReader.class);

    private YamlReader() {
    }

    /**
     * 解析配置文件为指定类对象
     *
     * @param <T> 泛型
     * @param fileName 配置文件
     * @param config 类
     * @return 类对象
     */
    public static <T> Optional<T> parseFile(@NonNull String fileName, @NonNull Class<T> config) {
        Optional<File> loadFile = loadFile(fileName);
        if (loadFile.isPresent()) {
            try (InputStream is = new FileInputStream(loadFile.get())) {
                Optional<T> configBean = Optional.of(new Yaml().loadAs(is, config));
                if (ValidatorUtil.isLegal(configBean.get())) {
                    return configBean;
                }
            } catch (FileNotFoundException e) {
                LOGGER.error("illegal file:{}", new File(fileName).getName());
            } catch (IOException e) {
                LOGGER.error("illegal file:{}", new File(fileName).getName());
            }
        }
        return Optional.empty();
    }

    private static Optional<File> loadFile(@NonNull String file) {
        return Optional.ofNullable(YamlReader.class.getClassLoader())
            .map(loader -> loader.getResource(file))
            .map(url -> new File(url.getFile()));
    }
}
