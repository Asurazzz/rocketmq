package com.ymj.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author : yemingjie
 * @date : 2021/9/3 22:28
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "tp_springboot_01", consumerGroup = "consumer_grp_03")
public class MyRocketListener implements RocketMQListener<String> {


    @Override
    public void onMessage(String message) {
        // 处理broker推送过来的消息
        log.info(message);
    }
}
