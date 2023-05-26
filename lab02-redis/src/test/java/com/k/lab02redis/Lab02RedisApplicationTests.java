package com.k.lab02redis;

import com.k.lab02redis.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class Lab02RedisApplicationTests {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
        redissonClient.getBucket("hello1").set("bug");
        String test = (String) redissonClient.getBucket("hello1").get();
        System.out.println(test);
    }


    @Test
    public void test() {
        long l = redissonClient.getKeys().deleteByPattern("shz" + "*");
        System.out.println(l);
    }

    /**
     * String
     */
    @Test
    public void stringTest() {
        RBucket<String> bucket = redissonClient.getBucket("lab02:string");
        LocalDate now = LocalDate.now();
        bucket.set(now.toString());
        System.out.println(bucket.get());
    }

    /**
     * list
     */
    @Test
    public void listTest() {
        RList<UserInfo> list = redissonClient.getList("lab02:list");
        int size = list.size();
        UserInfo userInfo = UserInfo.builder().id(size + 1).username("sz").password("s").phoneNumber("123").build();
        list.add(userInfo);
        System.out.println();
        List<UserInfo> userInfos = list.get(size, 0);
        System.out.println(userInfos);
        List<UserInfo> userInfoss = list.range(0, -1);
        System.out.println();
        System.out.println(userInfoss);
    }

    /**
     * map
     */
    @Test
    public void mapTest() {
        RMap<String, UserInfo> map = redissonClient.getMap("lab02:map");
        int size = map.size();
        int id = size + 1;
        UserInfo userInfo = UserInfo.builder().id(id).username("sz").password("s").phoneNumber("123").build();
        map.put(String.valueOf(id), userInfo);
        System.out.println();
        UserInfo userInfo1 = map.get(String.valueOf(id));
        System.out.println(userInfo1);
        Set<String> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(String.valueOf(i + 1));
        }
        Map<String, UserInfo> all = map.getAll(set);
        System.out.println();
        System.out.println(all);
    }


    /**
     * lock
     */
    @Test
    public void lockTest() {
        RLock lock = redissonClient.getLock("lab02:lock");
        lock.lock(10L, TimeUnit.SECONDS);
        System.out.println("------");
        lock.unlock();
    }


}
