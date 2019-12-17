/*
 * Copyright (c) Lizhiyang  xi'an China. 2019-2019. All rights reserved.
 */

package com.lzycug.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 功能描述
 *
 * @author 李志扬
 * @since 2019-12-17
 */
class TestJedis {
    public static void main(String[] args) {
        Optional<Jedis> jedisOptional = JedisPoolUtils.getJedis();
        Jedis jedis = null;
        if (jedisOptional.isPresent()) {
            jedis = jedisOptional.get();
        }

        // String类型存储
        jedis.set("username", "张三");

        // String获取
        String username = jedis.get("username");
        System.out.println(username);

        // 可以使用setex()方法存储可以指定过期时间的 key value
        // 将activecode：hehe键值对存入redis，并且20秒后自动删除该键值对
        jedis.setex("activecode", 20, "hehe");

        // Hash类型 Map(大小key)
        jedis.hset("user", "name", "李四");
        jedis.hset("user", "age", "18");
        jedis.hset("user", "address", "北京");

        // Hash获取
        String hget = jedis.hget("user", "name");
        System.out.println(hget);

        // 获取hash的所有map中的数据
        Map<String, String> userMap = jedis.hgetAll("user");
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + "------" + entry.getValue());
        }

        // List类型
        jedis.lpush("list", "a", "b", "c");// 从左边存
        jedis.rpush("list", "a", "b", "c");// 从右边存

        // list 范围获取
        List<String> mylist = jedis.lrange("list", 0, -1);
        System.out.println(mylist);

        // list 弹出
        String element1 = jedis.lpop("list");// c
        System.out.println(element1);

        String element2 = jedis.rpop("list");// c
        System.out.println(element2);

        // list 范围获取
        List<String> mylist2 = jedis.lrange("list", 0, -1);
        System.out.println(mylist2);

        // set 存储
        jedis.sadd("set", "java", "php", "c++");

        // set 获取
        Set<String> set = jedis.smembers("set");
        System.out.println(set);

        // sortedset 存储
        jedis.zadd("sortedset", 6, "德玛西亚");
        jedis.zadd("sortedset", 5, "青钢影");
        jedis.zadd("sortedset", 12, "岩溶巨兽");

        // sortedset 获取
        Set<String> mysortedset = jedis.zrange("mysortedset", 0, -1);
        System.out.println(mysortedset);

        // 资源释放
        JedisPoolUtils.releaseConn(jedis);
    }
}
