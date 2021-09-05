package com.ymj.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author : yemingjie
 * @date : 2021/9/4 13:47
 */
public class MyProducer1 {

    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_06");

        producer.setNamesrvAddr("192.168.195.131:9876");

        producer.start();

        Message message = null;

        for (int i = 0; i < 100; i++) {
            message = new Message(
                    "tp_demo_06",
                    "tag-" + (i % 3),
                    ("hello ymj - " + i).getBytes(StandardCharsets.UTF_8)
            );

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });

        }


        Thread.sleep(3_000);
        producer.shutdown();

    }
}
