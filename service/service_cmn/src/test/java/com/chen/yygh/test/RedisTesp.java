package com.chen.yygh.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTesp {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void redisTest(){
        redisTemplate.opsForValue().set("test","test");
    }
}
