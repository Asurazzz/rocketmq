package com.ymj.demo3;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

public class ProducerDelay {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("delay_producer");
        producer.setNamesrvAddr("192.168.150.11:9876;192.168.150.12:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msga = new Message(
                    "topic_delay",
                    "TagD",
                    ("message " + i).getBytes(StandardCharsets.UTF_8)
            );
            // 延迟是以消息为级别的
            msga.setDelayTimeLevel(1);
            // 没有直接进入到目标topic，而是进入延迟队列
            SendResult send = producer.send(msga);
            System.out.println(send);
        }
        System.in.read();
    }
}
