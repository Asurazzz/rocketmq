package com.ymj.demo1;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collection;

/**
 * @author yemingjie.
 * @date 2022/2/24.
 * @time 23:23.
 */
public class SimpleConsumerPullConsumer {

    public static void main(String[] args) throws Exception{
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("xxxx");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();

        Collection<MessageQueue> mqs = consumer.fetchMessageQueues("wula");
        mqs.forEach(messageQueue -> System.out.println(messageQueue));
    }
}
