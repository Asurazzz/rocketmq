package com.ymj.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Set;

/**
 * @author : yemingjie
 * @date : 2021/9/4 11:31
 */
public class MyConsumer {

    public static void main(String[] args) throws Exception{
        // 消息的拉取
        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer();
        // 消费的模式：广播
        pullConsumer.setMessageModel(MessageModel.BROADCASTING);
        // 消费的模式：集群
        pullConsumer.setMessageModel(MessageModel.CLUSTERING);

        Set<MessageQueue> messageQueues = pullConsumer.fetchSubscribeMessageQueues("tp_demo_05");
        for (MessageQueue messageQueue : messageQueues) {
            // 指定消息队列，指定标签过滤的表达式，消息偏移量和单次最大拉取的消息个数
            pullConsumer.pull(messageQueue, "*", 0L, 100);
        }
        // ==========================================================================

        // 消息的推送
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer();
        pushConsumer.setMessageModel(MessageModel.BROADCASTING);
        pushConsumer.setMessageModel(MessageModel.CLUSTERING);

        // 设置消费者的线程锁
        pushConsumer.setConsumeThreadMin(1);
        pushConsumer.setConsumeThreadMax(10);

        // subExpression表示对标签的过滤
        // * 表示不对消息进行标签过滤
        pushConsumer.subscribe("tp_demo_05", "*");

        // 设置消息批处理的一个批次中消息的最大个数
        pushConsumer.setConsumeMessageBatchMaxSize(10);

        // 设置完成之后调用start方法初始化并运行推送消息的消费者
        pushConsumer.start();

    }
}
