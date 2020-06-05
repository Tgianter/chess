package com.example.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.dto.MessageObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ServerEncoder implements Encoder.Text<MessageObject> {
    @Override
    public String encode(MessageObject messageObject) {

        return JSON.toJSONString(messageObject);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
