package com.ymj.demo1;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author yemingjie.
 * @date 2022/2/24.
 * @time 22:10.
 */
public class SimpleRocketMQProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ooxx");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTopic("wula");
            message.setTags("TagA");
            message.setBody(("ooxx" + i).getBytes(StandardCharsets.UTF_8));
            message.setWaitStoreMsgOK(true);

            // 第一版
            SendResult result = producer.send(message);
            System.out.println(result);
            System.out.println("-------------test1-------------");

            // 第二版
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {

                }
            });
            System.out.println("-------------test2-------------");

            // 第三版
            producer.sendOneway(message);
            System.out.println("-------------test3-------------");


        }

    }
}
