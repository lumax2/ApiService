package com.stream.nz.websocket.delayQueue;

import com.stream.nz.config.ThreadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
public class DelayQueueManager {
    private static final Logger log = LoggerFactory.getLogger(DelayQueueManager.class);

    private Executor executor;

    private Thread daemonThread;

    private static Map<String,DelayHealthQueueWorker> existDelayWorker = new ConcurrentHashMap();

    // 延迟队列
    private DelayQueue<DelayHealthQueueTask<?>> delayHealthQueue;

    public DelayQueueManager(ThreadConfig threadConfig) {
        executor = threadConfig.getAsyncExecutor();
        delayHealthQueue = new DelayQueue<>();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        daemonThread = new Thread(() -> {
            execute();
        });
        daemonThread.setName("HealthDelayQueueMonitor");
        daemonThread.start();
    }

    private void execute() {
        while (true) {
            try {
                // 从延时队列中获取任务
                DelayHealthQueueTask<?> delayOrderTask = delayHealthQueue.take();
                if (delayOrderTask != null) {
                    DelayHealthQueueWorker task = (DelayHealthQueueWorker) delayOrderTask.getTask();
                    if (null == task) {
                        continue;
                    }
                    executor.execute(task);
                }
            } catch (Exception e) {
                log.error("health delay queue error !",e);
            }
        }
    }

    /**
     * 添加任务
     * @param worker
     * @param time 延时时间
     * @param unit 时间单位
     */
    public void put(DelayHealthQueueWorker worker, long time, TimeUnit unit) {
        // 获取延时时间
        long timeout = TimeUnit.NANOSECONDS.convert(time, unit);
        // 将任务封装成实现Delayed接口的消息体
        DelayHealthQueueTask<?> delayOrder = new DelayHealthQueueTask<>(timeout, worker);
        delayHealthQueue.put(delayOrder);
        // 将消息体放到延时队列中
        if (existDelayWorker.containsKey(worker.getSession().getId())){
            return;
        }
        existDelayWorker.put(worker.getSession().getId(), worker);
    }

    /**
     * 删除任务
     *
     * @param sessionId
     * @return
     */
    public boolean removeWorker(String sessionId) {
        if (existDelayWorker.containsKey(sessionId)){
            DelayHealthQueueWorker remove = existDelayWorker.remove(sessionId);
            if (null!=remove){
                return true;
            }
        }
        return false;
    }
}
