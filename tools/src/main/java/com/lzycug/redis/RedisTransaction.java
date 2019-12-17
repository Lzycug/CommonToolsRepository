/*
 * Copyright (c) Lizhiyang  xi'an China. 2019-2019. All rights reserved.
 */

package com.lzycug.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * redis事务
 * Jedis事务处理:
 * 使用MULTI开启事务，EXEC提交事务。
 * 在执行事务期间使用discard可以回滚事务
 * 使用watch可以监视键，如果在事务之前和事务期间都修改同一键值，那么在事务之前的修改生效，事务期间的修改无效
 *
 * @author lzycug
 * @since 2019-12-17
 */
class RedisTransaction {
    /**
     * 执行逻辑：Jedis事务开始后，如果是Jedis内部方法错误，执行exec不影响其他正常语句的执行结果，执行成功的结果仍能提交到数据库。
     * 如果是Java语法错误比如被零除，就进入catch异常处理段，执行Jedis的discard()方法回滚所有事务
     */
    public static void jedisTransaction() {
        Optional<Jedis> optionalJedis = JedisPoolUtils.getJedis();
        Jedis jedis = null;
        if (optionalJedis.isPresent()) {
            jedis = optionalJedis.get();
        }

        // 开始事务，在执行exec之前都属于事务范围内
        Transaction tx = jedis.multi();
        boolean errFlag = false;
        try {
            tx.set("test1", "value1");
            tx.set("test2", "value2");

            // 对字符串进行算术运算，Jedis内部方法异常
            tx.incrBy("test1", 2);

            // 下面的运行时异常会导致程序进入catch段，然后执行discard()回滚所有事务
            // int x=10/0; 可以解注这条语句查看执行结果，别忘了清空数据库测试
            System.out.println("提交事务");

            // Jedis内部方法异常，提交事务执行成功的结果会存入redis数据库，执行失败的不执行
            List<Object> list = tx.exec();

            // 每条语句执行结果存入list中
            for (int i = 0; i < list.size(); i++) {
                System.out.println("list:" + list.get(i));
            }
        } catch (Exception e) {
            errFlag = true;
            // discard()方法在发生异常时可以回滚事务
            tx.discard();
            e.getMessage();
        } finally {
            if (errFlag == true) {
                System.out.println("发生异常时提交事务");
                tx.exec();
            }
            JedisPoolUtils.releaseConn(jedis);
        }
    }
}
