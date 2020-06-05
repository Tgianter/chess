package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 *
 * webSocket的连接配置
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter webSocket(){
        return new ServerEndpointExporter();
    }
}
