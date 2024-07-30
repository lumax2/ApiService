package com.stream.nz.websocket.config;

import com.stream.nz.websocket.WebSocketServer;
import com.stream.nz.websocket.delayQueue.DelayQueueManager;
import com.stream.nz.websocket.service.DuplexCommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Slf4j
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setRedisUtils(StringRedisTemplate redisTemplate) {
        WebSocketServer.redisTemplate = redisTemplate;
        log.info("WebSocketServer init redisTemplate");
    }


    @Autowired
    public void setDelayQueueManager(DelayQueueManager delayQueueManager) {
        WebSocketServer.delayQueueManager = delayQueueManager;
        log.info("WebSocketServer init delayQueueManager");
    }

    @Autowired
    public void setDuplexCommunicationService(DuplexCommunicationService duplexCommunicationService) {
        WebSocketServer.duplexCommunicationService = duplexCommunicationService;
        log.info("WebSocketServer init duplexCommunicationService");
    }




}
