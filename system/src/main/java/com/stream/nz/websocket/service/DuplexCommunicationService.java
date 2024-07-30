package com.stream.nz.websocket.service;

import cn.hutool.core.util.StrUtil;
import com.stream.nz.encrypt.util.RsaAesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * 双工通信服务基类
 */
@Service
@Slf4j
public class DuplexCommunicationService implements DuplexCommunication {


    @Override
    public void pushMessage(Session session, String unEncryptMsg){
        Map<String, String> pathParameters = session.getPathParameters();
        String s = RsaAesUtils.encryptByPrivateAes(unEncryptMsg, pathParameters.get("maxusJwt"));
        if (StrUtil.isEmpty(s)){
            log.error("push msg encrypt by privateAes get empty msg");
            return;
        }
        // 发送健康检查消息
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(s);
            } catch (IOException e) {
                log.error("exception happen when websocket session push message:{} ", s);
            }
        }
    };

    public void sendPing(Session session){
        synchronized (session) {
                ByteBuffer payload = ByteBuffer.wrap("Ping".getBytes());
            try {
                session.getBasicRemote().sendPing(payload);
            } catch (IOException e) {
                log.error("exception happen when websocket session send Ping ");
            }
        }
    }

}
