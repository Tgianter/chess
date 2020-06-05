package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
//    public static RedisUtil redisUtil;
//    @PostConstruct
//    public void init(){
//        redisUtil=this;
//        redisUtil.redisTemplate=this.redisTemplate;
//    }
//    RedisTemplate<String,Object> redisTemplate;
    //获得指定key的数据的失效时间
    synchronized public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    //判断缓存中是否有指定的key
    synchronized public boolean hasKey(String key){
        if(key!=null){
            return Objects.requireNonNull(redisTemplate.hasKey(key));
        }else{
            return false;
        }

    }
    //随机获得一个主键key
    synchronized public String getRandomKey() {
        return redisTemplate.randomKey();
    }

    synchronized public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    synchronized public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    synchronized public boolean expire(String key, long expire) {
        //设置键值为key的数据在redis中的失效时间，单位为SECONDS
        try {
            if (expire > 0) {
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    synchronized public void remove(String key) {
        redisTemplate.delete(key);

    }

    synchronized public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }
}
