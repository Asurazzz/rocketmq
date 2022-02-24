package com.ymj.demo1;

import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.util.Set;

/**
 * @author yemingjie.
 * @date 2022/2/24.
 * @time 22:45.
 */
public class SimpleAdminRocketMQ {
    public static void main(String[] args) throws Exception{
        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        admin.setNamesrvAddr("127.0.0.1:9876");
        admin.start();

        TopicList topicList = admin.fetchAllTopicList();
        Set<String> sets = topicList.getTopicList();
        sets.forEach(s -> System.out.println(s));

        System.out.println("topic route | info");
        TopicRouteData wula = admin.examineTopicRouteInfo("wula");
        System.out.println(wula);


    }
}
