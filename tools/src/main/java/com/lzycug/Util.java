
package com.lzycug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzycug.security.ValidatorUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 三方技能工具类
 *
 * @author x00285051
 * @since 2019-03-19
 */
public class Util {
    private static final Logger LOG = LogManager.getLogger(Util.class);

    private static final int SUB_LENGTH = 200;

    private static final Pattern PROPERTIES_FILE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5\\w.]{1,64}");

    private static final String PATTERN_MATCHER_TITLE = "(\\\\u(\\p{XDigit}{4}))";

    private static final String XML_FILE_SUFFIX = ".xml";

    private static final String ANONYMOUS_CHARACTER = "******";

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private static final JsonParser PARSER = new JsonParser();

    private static final int PLAIN_TEXT_SIZE = 7;

    /**
     * 二进制的第一位
     */
    private static final int FIRST_BIT = 1;

    /**
     * 二进制位数计算时减的值
     */
    private static final int REDUCE_BIT_NUMBER = 1;

    /**
     * 二进制移位的值
     */
    private static final int SHIFT_BIT_NUMBER = 1;

    /**
     * 字符串的值字段
     */
    private static final String VALUE_FIELD = "value";

    /**
     * 清理敏感字符串时的默认值
     */
    private static final int CHAR_DEFAULT_VALUE = 0;

    private static final int MATCH_SECOND_GROUP = 2;

    private static final int MATCH_FIRST_GROUP = 1;

    private static final int RADIX_DECIMAL = 16;

    private Util() {
    }

    /**
     * 判断是否为数字
     *
     * @param string 输入字符串
     * @return true:数字 false:非数字
     */
    public static boolean isNumber(String string) {
        return StringUtils.isNumeric(string);
    }

    /**
     * 对象的非空判断
     *
     * @param object 待判断对象
     * @param <T> 泛型，支持判空的类型，目前所有引用类型都支持，约束上界为Object
     * @return 是否为空
     */
    public static <T extends Object> boolean isEmpty(T object) {
        return ObjectUtils.isEmpty(object);
    }

    /**
     * 字符串的非空判断
     *
     * @param string 待判断字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String string) {
        return StringUtils.isEmpty(string);
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 处理的集合
     * @return 集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 对象的非空判断
     *
     * @param string 处理的值
     * @return 处理结果
     */
    public static boolean isBlank(String string) {
        return StringUtils.isBlank(string);
    }

    /**
     * 读取配置文件
     *
     * @param resourceName 文件名
     * @param classLoader 类加载器
     * @param encode 编码
     * @return 读取信息
     * @throws IOException 流异常，当无法正确关闭流的时候发生
     */
    public static Properties loadAllProperties(String resourceName, ClassLoader classLoader, String encode)
        throws IOException {
        Assert.notNull(resourceName, "resource name must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = ClassUtils.getDefaultClassLoader();
        }
        Enumeration<URL> urls = classLoaderToUse != null ? classLoaderToUse.getResources(resourceName)
            : ClassLoader.getSystemResources(resourceName);
        Properties properties = new Properties();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null || url.openConnection() == null) {
                continue;
            }
            URLConnection urlConnection = url.openConnection();
            ResourceUtils.useCachesIfNecessary(urlConnection);
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = null;
            try {
                if (resourceName.endsWith(XML_FILE_SUFFIX)) {
                    properties.loadFromXML(inputStream);
                } else {
                    if (isEmpty(encode)) {
                        properties.load(inputStream);
                    } else {
                        inputStreamReader = new InputStreamReader(inputStream, encode);
                        properties.load(inputStreamReader);
                    }
                }
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    LOG.info("close failed");
                }
                try {
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                } catch (IOException e) {
                    LOG.info("close failed");
                }
            }
        }
        checkLegality(properties);
        return properties;
    }

    private static void checkLegality(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (entry == null) {
                LOG.error("entry is null");
                continue;
            }
            if (!PROPERTIES_FILE_PATTERN.matcher(String.valueOf(entry.getKey())).matches()) {
                LOG.error("properties file key illegal, key:{}", entry.getKey());
            }
            if (!PROPERTIES_FILE_PATTERN.matcher(String.valueOf(entry.getValue())).matches()) {
                LOG.error("properties file value illegal, value:{}", entry.getValue());
            }
        }
    }

    /**
     * 2018-08-15 00:00:00转换成long类型
     *
     * @param date 字符串参数
     * @return 解析后的时间
     * @throws ParseException 解析异常，当无法正确解析字符串时候发生
     */
    public static long parseLong(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return dateFormat.parse(date).getTime();
    }

    /**
     * 将对象转换为json字符串
     *
     * @param object 转换为json字符串的对象
     * @param <T> 泛型
     * @return 转换的json字符串
     */
    public static <T> String toJson(T object) {
        return GSON.toJson(object);
    }

    /**
     * 将json字符串转为对应对象
     *
     * @param json 转换为对象的json字符串
     * @param <T> 泛型
     * @param clazz 类字节码对象
     * @return 转换的对应对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * 将json字符串转为json数组
     *
     * @param json 转换为数组的json字符串
     * @return 转换的json数组
     */
    public static JsonArray getAsJsonArray(String json) {
        if (json == null) {
            return new JsonArray();
        }
        return PARSER.parse(json).getAsJsonArray();
    }

    /**
     * 将json字符串转为json对象
     *
     * @param json 转换为json对象的json字符串
     * @return 转换的json对象
     */
    public static JsonObject getAsJsonObject(String json) {
        if (json == null) {
            return new JsonObject();
        }
        return PARSER.parse(json).getAsJsonObject();
    }

    /**
     * 加载yaml配置文件
     *
     * @param filePath 文件路径
     * @param <T> 泛型
     * @param type 字节码类型
     * @return yaml配置文件
     */
    public static <T> Optional<T> loadYaml(String filePath, Class<T> type) {
        Optional<File> loadFile = loadFile(filePath);
        if (!loadFile.isPresent()) {
            LOG.error("load file failed");
            return Optional.empty();
        }
        File file = loadFile.get();
        try (InputStream inputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            T bean = yaml.loadAs(inputStream, type);
            if (!ValidatorUtil.isLegal(bean)) {
                LOG.error("yaml bean illegal, beanName:{}", bean.getClass().getName());
                return Optional.empty();
            }
            return Optional.ofNullable(bean);
        } catch (IOException e) {
            LOG.error("loadYaml failed");
        }
        return Optional.empty();
    }

    /**
     * 加载yaml配置文件
     *
     * @param filePath 文件路径
     * @param <T> 泛型
     * @return yaml配置文件
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> loadYaml(String filePath) {
        Optional<File> loadFile = loadFile(filePath);
        if (!loadFile.isPresent()) {
            LOG.error("load file failed");
            return Optional.empty();
        }
        File file = loadFile.get();
        try (InputStream inputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return Optional.ofNullable(yaml.load(inputStream));
        } catch (IOException e) {
            LOG.error("loadYaml failed");
        }
        return Optional.empty();
    }

    /**
     * 加载文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static Optional<File> loadFile(String filePath) {
        ClassLoader classLoader = Util.class.getClassLoader();
        if (classLoader == null) {
            LOG.error("classLoader is null");
            return Optional.empty();
        }
        URL url = classLoader.getResource(filePath);
        if (url == null) {
            LOG.error("url is null");
            return Optional.empty();
        }
        String path = url.getPath();
        try {
            path = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            LOG.error("load file failed");
        }
        return Optional.ofNullable(new File(path));
    }

    /**
     * 克隆一个新MAP
     * 注意：无论入参是MAP的何种实现，clone的结果MAP都是HashMap
     *
     * @param prototype 克隆的源MAP
     * @param <T> 泛型，MAP的Value必须自行实现SecureCloneable接口，确保安全的序列化
     * @return 克隆MAP
     */
    public static <T extends SecureCloneable<T>> Optional<Map<String, T>> secureClone(Map<String, T> prototype) {
        if (CollectionUtils.isEmpty(prototype)) {
            return Optional.empty();
        }
        Map<String, T> results = new HashMap<>(prototype.size());
        prototype.forEach((key, value) -> {
            secureClone(value).ifPresent(item -> {
                results.put(key, item);
            });
        });
        return Optional.of(results);
    }

    /**
     * 克隆一个新LIST
     * 注意：无论入参是List的何种实现，clone的结果List都是ArrayList
     *
     * @param prototype 克隆的源LIST
     * @param <T> 泛型，LIST元素必须自行实现SecureCloneable接口，确保安全的序列化
     * @return 克隆LIST
     */
    public static <T extends SecureCloneable<T>> Optional<List<T>> secureClone(List<T> prototype) {
        if (CollectionUtils.isEmpty(prototype)) {
            return Optional.empty();
        }
        List<T> results = new ArrayList<>(prototype.size());
        prototype.forEach(one -> {
            secureClone(one).ifPresent(item -> {
                results.add(item);
            });
        });
        return Optional.ofNullable(results);
    }

    /**
     * 克隆一个新对象
     *
     * @param prototype 克隆的源对象
     * @param <T> 泛型，被clone对象必须自行实现SecureCloneable接口，确保安全的序列化
     * @return 克隆对象
     */
    public static <T extends SecureCloneable<T>> Optional<T> secureClone(T prototype) {
        if (prototype == null) {
            return Optional.empty();
        }

        // 已有泛型约束类型，无需进一步判断
        return Optional.ofNullable(prototype.clone());
    }

    /**
     * 克隆一个新MAP
     *
     * @param prototype 克隆的源MAP
     * @param <T> 泛型，MAP的KEY必须可序列化
     * @param <U> 泛型，MAP的VALUE必须可序列化
     * @return 克隆MAP
     */
    public static <T extends Serializable, U extends Serializable> Optional<Map<T, U>> clone(Map<T, U> prototype) {
        return cloneBySerialization(prototype);
    }

    /**
     * 克隆一个新LIST
     *
     * @param prototype 克隆的源LIST
     * @param <T> 泛型，LIST元素必须支持序列化
     * @return 克隆LIST
     */
    public static <T extends Serializable> Optional<List<T>> clone(List<T> prototype) {
        return cloneBySerialization(prototype);
    }

    /**
     * 克隆一个新对象
     * 说明：本方法仅适用于需要序列化接口的类型和数组进行深度复制。
     * 根据安全规范，这些可序列化对象不能包含敏感信息。
     * 如需包含敏感信息的对象拷贝，应实现SecureCloneable接口并调用secureClone。
     *
     * @param prototype 克隆的源对象
     * @param <T> 泛型
     * @return 克隆对象
     */
    public static <T> Optional<T> cloneBySerialization(T prototype) {
        if (prototype == null) {
            return Optional.empty();
        }
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(prototype);
            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            objectInputStream = new SecureObjectInputStream(byteArrayInputStream);
            Object result = objectInputStream.readObject();
            if (prototype.getClass().isInstance(result)) {
                // 目前本方法只允许实现序列化接口的对象进行复制，且复制结果必然与源对象类型一致。
                // 此处类型已判断，类型转换是安全的。
                @SuppressWarnings("unchecked")
                T targetObject = (T) result;
                return Optional.ofNullable(targetObject);
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error("clone failed!!");
            return Optional.empty();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                LOG.error("close objectInputStream failed!!");
            }
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                LOG.error("close byteArrayInputStream failed!!");
            }
        }
    }

    /**
     * Unicode编码转中文
     *
     * @param title 新闻标题
     * @return string 转成中文后的标题
     */
    public static String unicodeToChinese(String title) {
        String titleTemp = title;
        try {
            Pattern pattern = Pattern.compile(PATTERN_MATCHER_TITLE);
            Matcher matcher = pattern.matcher(title);
            char word;
            while (matcher.find()) {
                String secondGroup = matcher.group(MATCH_SECOND_GROUP);
                word = (char) Integer.parseInt(secondGroup, RADIX_DECIMAL);
                String firstGroup = matcher.group(MATCH_FIRST_GROUP);
                titleTemp = title.replace(firstGroup, word + StringUtils.EMPTY);
            }
        } catch (NumberFormatException e) {
            LOG.error("tencent news title unicode to chinese fail exception:{}", ExceptionUtils.getStackTrace(e));
        }
        return titleTemp;
    }

    /**
     * 转UTF-8编码
     *
     * @param value 需要编码的值
     * @return 转码后的值
     */
    public static String encode(String value) {
        String encode = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(value)) {
            try {
                encode = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                LOG.error("encoding UTF-8 exception:{}", ExceptionUtils.getStackTrace(e));
                encode = value;
            }
        }
        return encode;
    }

    /**
     * 获取一个二进制数第几位的值
     *
     * @param originalNumber 数值
     * @param bitNumber 第几位
     * @return 第几位的值
     */
    public static int getBitValueInNum(int originalNumber, int bitNumber) {
        if (bitNumber < FIRST_BIT) {
            return originalNumber;
        }
        return (setBitValueOneInNum(~originalNumber, bitNumber) & originalNumber) == 0 ? 0 : 1;
    }

    /**
     * 设置一个二进制数第几位的值为1
     *
     * @param originalNumber 数值
     * @param bitNumber 第几位
     * @return 设置后的值
     */
    public static int setBitValueOneInNum(int originalNumber, int bitNumber) {
        if (bitNumber < FIRST_BIT) {
            return originalNumber;
        }
        int realBit = bitNumber - REDUCE_BIT_NUMBER;
        return SHIFT_BIT_NUMBER << realBit | originalNumber;
    }

    /**
     * 设置一个二进制数第几位的值为0
     *
     * @param originalNumber 数值
     * @param bitNumber 第几位
     * @return 设置后的值
     */
    public static int setBitValueZeroInNum(int originalNumber, int bitNumber) {
        if (bitNumber < FIRST_BIT) {
            return originalNumber;
        }
        return ~setBitValueOneInNum(~originalNumber, bitNumber);
    }

    /**
     * 字符串转换成成浮点型，异常时返回0
     *
     * @param inputValue 值的字符串格式
     * @return 浮点型值
     */
    public static float getFloatValue(String inputValue) {
        float result = 0.0F;
        if (StringUtils.isNotEmpty(inputValue)) {
            try {
                result = Float.parseFloat(inputValue);
            } catch (NumberFormatException e) {
                LOG.error("number format error:{}", inputValue);
            }
        }
        return result;
    }

    /**
     * 清理敏感字符串值
     *
     * @param value 敏感字符串值
     */
    public static void destroySensitiveString(String value) {
        if (StringUtils.isEmpty(value)) {
            LOG.error("sensitiveString is empty");
            return;
        }
        try {
            Field valueFieldOfString = String.class.getDeclaredField(VALUE_FIELD);
            valueFieldOfString.setAccessible(true);
            Object valueObject = valueFieldOfString.get(value);
            if (valueObject instanceof char[]) {
                char[] charValues = (char[]) valueObject;
                for (int i = 0; i < charValues.length; i++) {
                    charValues[i] = CHAR_DEFAULT_VALUE;
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            LOG.error("destroySensitiveString error");
        }
    }

    /**
     * 清除敏感信息
     *
     * @param data 待清零敏感数据
     */
    public static void cleanBytes(byte[] data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
    }
}
