package com.stream.nz.websocket;

import com.alibaba.fastjson2.JSON;
import com.stream.nz.encrypt.util.RsaAesUtils;
import com.stream.nz.token.JwtTools;
import com.stream.nz.token.model.dto.OverseaClaims;
import com.stream.nz.websocket.delayQueue.DelayHealthQueueWorker;
import com.stream.nz.websocket.delayQueue.DelayQueueManager;
import com.stream.nz.websocket.service.DuplexCommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenghao
 * @Date 2023/12/20 16:12
 */

@ServerEndpoint(value = "/ws/{maxusJwt}")
@Service
@Slf4j
public class WebSocketServer {

    public static StringRedisTemplate redisTemplate;

    public static DelayQueueManager delayQueueManager;

    public static DuplexCommunicationService duplexCommunicationService;

    @OnOpen
    public void onOpen(Session session, @PathParam("maxusJwt") String maxusJwt) {
        log.info("server websocket on open with serviceCode【{}】", JwtTools.parsePayload(maxusJwt, OverseaClaims.class).getServiceCode());
        DelayHealthQueueWorker worker = new DelayHealthQueueWorker();
        worker.setSession(session);
        worker.setDelayQueueManager(delayQueueManager);
        worker.setDuplexCommunicationService(duplexCommunicationService);
        worker.setStringRedisTemplate(redisTemplate);
        worker.setHealthCheckGapNanoSeconds(5000);
        // Websocket 上线日志MQ广播
        delayQueueManager.put(worker,5000, TimeUnit.MILLISECONDS);
    }


    @OnMessage
    public void onMessage(String message, Session session, @PathParam("maxusJwt") String maxusJwt) throws IOException {
        String decrypted = RsaAesUtils.decryptByPrivateKeyAes(message, maxusJwt);
        log.info("server receive this message : [{}]",decrypted);

        // response message
        duplexCommunicationService.pushMessage(session,"server response this message when receive:" + decrypted);
    }

    @OnError
    public void onError(Session session, Throwable error,@PathParam("maxusJwt") String maxusJwt) {
        if (null== maxusJwt){
            log.error("websocket on error", error);
        }else {
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            log.error("websocket on error OverseaClaims[{}]", null==overseaClaims?"":overseaClaims.getServiceCode());
        }
        boolean b = delayQueueManager.removeWorker(session.getId());
        log.info("on Error remove delay queue worker[{}]", b);
    }

    @OnClose
    public void onClose(Session session,CloseReason closeReason, @PathParam("maxusJwt") String maxusJwt) {
        if (null== maxusJwt){
            log.error("websocket on close[{}]",JSON.toJSONString(closeReason));
        }else {
            OverseaClaims overseaClaims = JwtTools.parsePayload(maxusJwt, OverseaClaims.class);
            log.error("websocket on error OverseaClaims[{}],[{}]", null==overseaClaims?"":overseaClaims.getServiceCode(),JSON.toJSONString(closeReason));
        }
        boolean b = delayQueueManager.removeWorker(session.getId());
        log.info("on close remove delay queue worker[{}]", b);
    }





}
