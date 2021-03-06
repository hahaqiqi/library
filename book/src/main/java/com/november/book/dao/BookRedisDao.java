package com.november.book.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author skrT
 * @create 2018/11/23 11:53
 */

@Component
public class BookRedisDao {

    @Autowired
    private StringRedisTemplate template;

    /**
     * 储存值
     * */
    public  void setKey(String key,String value){
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key,value);
    }

    /**
     *  获得值
     * */
    public String getValue(String key){
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    /**
     *  储存限时的值
     * */
    public void setExKey(String key,String value,long time,TimeUnit timeUnit){
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key,value,time, timeUnit);
    }

    /**
     *  删除值
     * */
    public void delKey(String key){
        template.delete(key);
    }

}