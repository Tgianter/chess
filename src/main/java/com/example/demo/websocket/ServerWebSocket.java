package com.example.demo.websocket;

import com.example.demo.service.UserService;
import com.example.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
//前端可以通过这个路径，连接到webSocket服务器，其中{userId}要换成当前用户对应的id
@ServerEndpoint(value = "/socket/{userId}",encoders = {ServerEncoder.class})
@Slf4j
public class ServerWebSocket {
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
//    RedisUtil redisUtil=new RedisUtil();
//    private RedisService redisService;
    //存储连接到websocket服务端的客户端用户的session
    private Session session;
    //存储连接的客户端的名字
    private String userId;
    private static ConcurrentHashMap<String , ServerWebSocket> webSocket= new ConcurrentHashMap<>();
    //当客户端与websocket服务器建立连接后，会自动调用该方法
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId){

        this.session=session;
        this.userId=userId;
        webSocket.put(userId,this);
//        for(Integer i:webSocket.keySet()){
//            System.out.println(i);
//        }

        log.info(userId+"成功连接websocket服务器");
    }
    //当客户端与服务端断开连接后，会调用该方法
    @OnClose
    public void onClose(){
        webSocket.remove(userId);
        //从缓存中移除用户
//        redisService.remove(userId);
    }
    //当客户端向websocket发送消息时会被调用
    @OnMessage
    public void onMessage(String message){
        log.info("服务器已接收到，客户端发送的消息"+message);

    }
    //向指定标识符为userId的客户端发送消息
    public void AppointSending(String userId,Object message){
        try {
//            webSocket.get(userId).session.getBasicRemote().sendText(message);
            webSocket.get(userId).session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
    //向所有连接在websocket的客户端发送消息
    public void GroupSending(String message){
        for(String str:webSocket.keySet()){
            try {
                webSocket.get(str).session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
