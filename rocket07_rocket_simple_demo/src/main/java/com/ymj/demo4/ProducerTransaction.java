package com.ymj.demo4;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProducerTransaction {
    public static void main(String[] args) throws Exception{
        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer");
        producer.setNamesrvAddr("192.168.150.11:9876;192.168.150.12:9876");

        // 1.半消息成功了才能执行本地事务，也需要监听
        // 2.半消息的回调要被监听到
        producer.setTransactionListener(new TransactionListener() {
            /**
             * 本地事务应该干啥？
             * 从成本的角度来思考，三个地方可以传导业务需要的参数
             * 1.message的body，网络带宽，污染了body的编码
             * 2.userProperty 通过网络传递给consumer
             * 3.arg方式 local
             * @param message
             * @param o
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                // send ok half消息
//                String action = message.getProperty("action");
                String action = (String) o;
                String transactionId = message.getTransactionId();
                /**
                 * 状态有两个：
                 * rocketmq的half半消息，这个状态驱动rocketmq，回查producer
                 * service应该是无状态的，那么应该吧transactionId 随着本地事务的执行写入事件状态表中
                 */
                switch (action) {
                    case "0":
                        System.out.println(Thread.currentThread().getName() + "send half: Async api call...action： 0");
                        // rocketmq 会回调check
                        return LocalTransactionState.UNKNOW;
                    case "1":
                        System.out.println(Thread.currentThread().getName() + "send half: LocalTransaction failed...action: 1");
                        /**
                         * transaction.begin
                         * throw...
                         * transaction.rollback
                         */
                        // 观察consumer是消费不到rollback的message的
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    case "2":
                        System.out.println(Thread.currentThread().getName() + "send half: LocalTransaction ok...action: 2");
                        /**
                         * transaction.begin
                         * throw...
                         * transaction.ok
                         */
                        // 观察consumer是消费的到的，只不过还要验证，中途会不会做check
                        try {
                            Thread.sleep(1000 * 10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return LocalTransactionState.COMMIT_MESSAGE;
                }
                return null;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println(Thread.currentThread().getName());
                // call back check
                String transactionId = messageExt.getTransactionId();
                String action = messageExt.getProperty("action");
                int times = messageExt.getReconsumeTimes();
                switch (action) {
                    case "0":
                        System.out.println(Thread.currentThread().getName() + " check action： 0, UNKNOWN : " + times);
                        if (times < 2) {
                            return LocalTransactionState.UNKNOW;
                        } else {
                            return LocalTransactionState.COMMIT_MESSAGE;
                        }
                    case "1":
                        System.out.println(Thread.currentThread().getName() + " check action: 1, ROLLBACK : " + times);
                        // 观察事务表
                        return LocalTransactionState.UNKNOW;
                    case "2":
                        System.out.println(Thread.currentThread().getName() + "check action: 2, COMMIT : " + times);
                        // check都是观察事务表的
                        return LocalTransactionState.UNKNOW;
                }
                return null;
            }
        });

        // need Thread 不是必须要去配置的
        producer.setExecutorService(new ThreadPoolExecutor(
                1,
                Runtime.getRuntime().availableProcessors(),
                2000,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                         return new Thread(r, "Transaction Thread");
                    }
                }
        ));

        producer.start();

        for (int i = 0;i < 10;i++) {
            Message msgt = new Message(
                    "topic_transaction",
                    "TagT",
                    "key:" + i,
                    ("message : " + i).getBytes(StandardCharsets.UTF_8)
            );
            msgt.putUserProperty("action", i % 3+"");
            // 发送的是半消息 String类型
            TransactionSendResult res = producer.sendMessageInTransaction(msgt, i % 3 +"");
            System.out.println(res);
        }
        System.in.read();
    }

}
