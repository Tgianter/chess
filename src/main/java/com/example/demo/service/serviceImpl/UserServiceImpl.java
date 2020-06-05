package com.example.demo.service.serviceImpl;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Boolean insert(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public User selectUserByOpenid(String openid) {
        return userMapper.selectUserByOpenId(openid);
    }
}
