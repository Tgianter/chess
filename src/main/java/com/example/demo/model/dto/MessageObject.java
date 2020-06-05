package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 玩家收到的消息格式封装
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageObject {
    /**
     *   收到的消息类型码，
     *   0：玩家收到的的对手的走棋位置信息
     *   1：对手发送的投降消息
     */
    private int messageCode;
    /**
     *   当messageCode=0时，data就是对手的走棋位置信息
     *   当messageCode=1时，data就是后台系统发给玩家的对手的投降消息
     */
    private PositionObject data;
    /**
     *
     *   消息的辨识码，当前端要使用全局变量中的reciveMessage时，
     *   可以把这个属性的值和之前收到的值进行比对，
     *   如果相同，则说明没收到新的消息；如果不同，则说明收到了新的消息
     *
     */
    private String identCode;
}
