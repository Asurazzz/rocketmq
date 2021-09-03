package com.ymj.rocket.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.Set;

/**
 * @Classname MyConsumer
 * @Description 拉取消息的消费者
 * @Date 2021/9/3 14:17
 * @Created by yemingjie
 */
public class MyPullConsumer {

    public static void main(String[] args) throws Exception{
        // 拉取消息的消费者实例化，同时指定消费组名称
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("consumer_grp_01");
        // 设置nameserver的地址
        consumer.setNamesrvAddr("192.168.195.131:9876");

        // 对消费者进行初始化，然后就可以消费了
        consumer.start();

        // 获取指定主题的消息队列集合
        final Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("tp_demo_01");

        // 遍历该主题的各个消息队列进行消费
        for (MessageQueue messageQueue : messageQueues) {
            // 第一个参数是MessageQueue对象，代表了当前主题的一个消息队列
            // 第二个参数是一个表达式，对接收的消息按照tag进行过滤
            //      null 或者 "*"表示不对tag进行过滤， 支持"tag1 || tag2 || tag3"
            // 第三个参数是消息的偏移量，从这里开始消费
            // 第四个参数表示每次最多拉取多少条消息
            final PullResult result = consumer.pull(messageQueue, "*", 0, 10);

            // 获取从指定消息队列中拉取的消息
            List<MessageExt> msgFoundList = result.getMsgFoundList();
            System.out.println("message******queue****** " + messageQueue);
            if (msgFoundList == null) {
                continue;
            }
            for (MessageExt messageExt : msgFoundList) {
                System.out.println(messageExt);
                System.out.println(new String(messageExt.getBody(), "utf-8"));
            }
        }

        consumer.shutdown();

    }
}
