package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= DemoApplication.class)
class DemoApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        User user=new User("12324","luoting","d:/image/jkds/1.img");
        Boolean result=userMapper.insertUser(user);
        System.out.println("插入成功"+result);

    }

}
