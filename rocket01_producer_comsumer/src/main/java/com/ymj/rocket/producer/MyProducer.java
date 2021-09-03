package com.ymj.rocket.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @Classname MyProducer
 * @Description TODO
 * @Date 2021/9/3 10:05
 * @Created by yemingjie
 */
public class MyProducer {
    public static void main(String[] args) throws Exception{
        // 实例化生产者的同时，指定了生产组名称
        DefaultMQProducer producer = new DefaultMQProducer("myproducer_grp_01");

        // 指定NameServer的地址
        producer.setNamesrvAddr("192.168.1.241:9876");

        // 对生产者进行初始化，开始使用
        producer.start();

        // 创建消息，第一个参数是主题名称，第二个参数是消息内容
        Message message = new Message("tp_test_01",
                "hello_ymj_01".getBytes(StandardCharsets.UTF_8));
        // 发送消息
        final SendResult result = producer.send(message);
        System.out.println(result);

        // 关闭生产者
        producer.shutdown();

    }
}
