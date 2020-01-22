package com.lzycug;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 带安全检查的ObjectInputStream
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class SecureObjectInputStream extends ObjectInputStream {
    private static final String WHITE_LIST_PACKAGE_FIRST = "java.util.";

    private static final String WHITE_LIST_PACKAGE_SECOND = "java.io.";

    private static final String WHITE_LIST_PACKAGE_THIRD = "org.apache.";

    private static final String WHITE_LIST_PACKAGE_FOURTH = "io.vertx.";

    private static final String WHITE_LIST_PACKAGE_FIFTH = "java.lang.";

    private static final String WHITE_LIST_PACKAGE_SIXTH = "com.huawei.";

    private static final String WHITE_LIST_SELF_BEAN_LIST = "[Lcom.huawei.";

    private static final String[] WHITE_ALL_LIST_ARRAY =
        new String[] {WHITE_LIST_PACKAGE_FIRST, WHITE_LIST_PACKAGE_SECOND, WHITE_LIST_PACKAGE_THIRD,
            WHITE_LIST_PACKAGE_FOURTH, WHITE_LIST_PACKAGE_FIFTH, WHITE_LIST_PACKAGE_SIXTH, WHITE_LIST_SELF_BEAN_LIST};

    private static final Logger LOG = LogManager.getLogger(SecureObjectInputStream.class);

    /**
     * 构造函数
     *
     * @throws IOException 表示发生某种I/O异常的信号，中断I/O操作。
     * @throws SecurityException 由安全管理器抛出以指示安全违规
     */
    public SecureObjectInputStream() throws IOException, SecurityException {
        super();
    }

    /**
     * 构造函数
     *
     * @param inputStream 输入流
     * @throws IOException 表示发生某种I/O异常的信号，中断I/O操作。
     */
    public SecureObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    /**
     * 白名单验证，目前仅支持白名单中的类和包执行readObject方法
     *
     * @param streamClass 序列化的类描述符
     * @throws IOException 表示发生某种I/O异常的信号，中断I/O操作。
     * @throws ClassNotFoundException 当应用程序尝试通过其加载类时抛出，无法找到指定的类异常
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass streamClass) throws IOException, ClassNotFoundException {
        if (streamClass == null || streamClass.getName() == null) {
            LOG.error("unSafe deserialization:objectName is empty");
            throw new ClassNotFoundException("objectName is empty");
        }
        for (String packageName : WHITE_ALL_LIST_ARRAY) {
            if (streamClass.getName().startsWith(packageName)) {
                return super.resolveClass(streamClass);
            }
        }
        LOG.error("unSafe deserialization:{}", streamClass.getName());
        throw new ClassNotFoundException(streamClass.getName() + " not in whiteList");
    }
}
