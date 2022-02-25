package com.ymj.demo2;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author yemingjie.
 * @date 2022/2/25.
 * @time 09:20.
 */
public class SimpleRocketMQProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("ymj");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTopic("hello");
            message.setTags("TagA");
            message.setBody(("world-" + i).getBytes(StandardCharsets.UTF_8));
            message.setWaitStoreMsgOK(true);

            SendResult result = producer.send(message);
            System.out.println(result);

        }

    }
}
