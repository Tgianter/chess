package com.example.demo.service;

import com.example.demo.model.User;


public interface UserService {
    //插入用户
    Boolean insert(User user);
    //查询用户
    User selectUserByOpenid(String openid);
}
