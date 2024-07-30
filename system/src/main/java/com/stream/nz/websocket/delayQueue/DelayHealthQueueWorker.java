package com.stream.nz.websocket.delayQueue;

import cn.hutool.core.date.DateUtil;
import com.stream.nz.websocket.service.DuplexCommunicationService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.websocket.Session;
import java.util.concurrent.TimeUnit;

@Data
@EqualsAndHashCode
public class DelayHealthQueueWorker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DelayHealthQueueWorker.class);

    private DelayQueueManager delayQueueManager;

    private DuplexCommunicationService duplexCommunicationService;

    private StringRedisTemplate stringRedisTemplate;

    private Session session;

    private Integer healthCheckGapNanoSeconds;

    private int retryCount = 0;


    @Override
    public void run() {
        try {
            if (null!= session && session.isOpen()) {
                duplexCommunicationService.pushMessage(session, DateUtil.now() + "{JSONString pushed by server}");
            }
            delayQueueManager.put(this, healthCheckGapNanoSeconds, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            log.error("delay queue consume error ", e);
        }
    }


}
