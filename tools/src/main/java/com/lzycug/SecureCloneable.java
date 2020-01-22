package com.lzycug;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 安全的克隆能力接口
 * 安全规范要求敏感信息对象禁止通过对象序列化直接复制克隆对象
 * 作为替代，实现该接口的类应该自行实现安全的clone方法。
 *
 * @author lzycug
 * @param <T> 泛型约束，必须实现Cloneable接口
 * @since 2020-01-22
 */
public interface SecureCloneable<T extends Cloneable> extends Cloneable {
    /**
     * 日志对象，由各实现类在克隆失败时记录日志
     */
    Logger LOG = LogManager.getLogger(SecureCloneable.class);

    /**
     * 安全的克隆方法
     * 因为Object的clone方法无法约束类型，此处按要求加以限制
     *
     * @return 克隆对象
     */
    T clone();
}
