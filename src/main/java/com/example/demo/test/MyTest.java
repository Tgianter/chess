package com.example.demo.test;

import com.example.demo.DemoApplication;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.dto.OnlineUser;
import com.example.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes= DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;
    @Test
    public void dataTest() {
//        User user=new User("12324","luoting","d:/image/jkds/1.img");
//        Boolean result=userMapper.insertUser(user);
//        System.out.println("插入成功"+result);
//        User user=userMapper.selectUserByOpenId("12324");
//        System.out.println(user);
        String userId="nsdkfhwuo34120nfsd";
        OnlineUser onlineUser;
        onlineUser=new OnlineUser(userId,Boolean.FALSE,null);
        //将用户id存入缓存中
        redisUtil.set(userId,onlineUser);

        redisUtil.expire(userId,300000);

        onlineUser=new OnlineUser("12345",Boolean.FALSE,null);
        redisUtil.set("12345",onlineUser);
        redisUtil.expire("12345",300000);
        log.info(userId+"成功连接websocket服务器");
    }
}
