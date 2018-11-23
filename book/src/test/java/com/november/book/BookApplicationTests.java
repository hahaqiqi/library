package com.november.book;

import com.november.book.dao.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookApplicationTests {

    @Resource
    RedisDao redisDao;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1(){
        redisDao.setKey("a","111");
        System.out.println(redisDao.getValue("a"));
    }

}
