package com.ymj.demo2;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collection;

/**
 * @author yemingjie.
 * @date 2022/2/25.
 * @time 09:41.
 */
public class SimplePullConsumer {
    public static void main(String[] args) throws Exception{
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("xxxxx");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();

        Collection<MessageQueue> mqs = consumer.fetchMessageQueues("hello");
        mqs.forEach(messageQueue -> System.out.println(messageQueue));

    }
}
