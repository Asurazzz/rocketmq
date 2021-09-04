package com.ymj.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author : yemingjie
 * @date : 2021/9/4 11:00
 */
public class MyProducer {

    public static void main(String[] args) throws Exception{

        // 该producer是线程安全的，可以多线程使用
        // 建议使用多个producer实例发送
        // 实例化生产者实例，同时设置生产组名称
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_04");

        // 设置实例名称，一个JVM中如果有多个生产者，可以通过实例名称来区分
        // 默认DEFAULT
        producer.setInstanceName("producer_grp_04_01");

        // 设置同步发送重试的次数
        producer.setRetryTimesWhenSendFailed(2);

        // 设置异步发送重试的次数
        producer.setRetryTimesWhenSendAsyncFailed(2);

        // 设置nameserver的地址
        producer.setNamesrvAddr("192.168.195.131:9876");
        // 对生产者进行初始化
        producer.start();

        // 组装消息
        Message message = new Message("tp_demo_04", "hello ymj 04".getBytes(StandardCharsets.UTF_8));

        // 同步发送消息，如果发送消息失败，则按照setRetryTimesWhenSendFailed设置的次数来进行重试
        // broker中可能会有重复的消息，由开发者进行处理
        SendResult result = producer.send(message);

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 成功的处理逻辑
            }

            @Override
            public void onException(Throwable throwable) {
                // 失败的处理逻辑
                // 重试次数耗尽，发生的异常
            }
        });

        // 将消息放到socket缓冲区就返回，没有返回值，不会等待broker的响应，
        // 速度快会丢消息，单向发送
        producer.sendOneway(message);

        SendStatus sendStatus = result.getSendStatus();

    }
}
