package com.example.demo.model.dto;

import com.example.demo.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultUser {
    private String userOpenid;
    private String userName;
    private String userHead;
    private int flag;
    public ResultUser(User user){
        this.userOpenid=user.getUserOpenid();
        this.userHead=user.getUserHead();
        this.userName=user.getUserName();
    }
}
