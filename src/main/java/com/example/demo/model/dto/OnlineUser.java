package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser implements Serializable {
//    private static final long serialVersionUID =-3426469906980190971;
    private String open_id;
    private boolean isWaring;
    private String enemyId=null;
}
