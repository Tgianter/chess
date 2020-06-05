package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private String userOpenid;
    private String userName;
//    private String userGender;
    private String userHead;
//    private String userProvince;
//    private Date userNewLogin;


}
