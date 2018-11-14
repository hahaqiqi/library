package com.november.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @author skrT
 * @create 2018/11/10 10:07
 */
//  redis缓存
@Component("redisPool")
@Slf4j
public class RedisPool {

//    @Resource(name = "shardedJedisPool",)
    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance(){
        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis){
        try{
            if(null != shardedJedis){
                shardedJedis.close();
            }
        }catch(Exception e){
            log.error("close redis resource exception :"+e);
        }
    }
}
