package com.ymj;

import com.ymj.MyRocketProducerApplication;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Classname MyRocketProducerApplicationTest
 * @Description TODO
 * @Date 2021/9/3 18:19
 * @Created by yemingjie
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyRocketProducerApplication.class})
public class MyRocketProducerApplicationTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendMessage() {
        // 第一个参数是topic名称
        // 第二个参数是消息内容
        this.rocketMQTemplate.convertAndSend(
                "tp_springboot_01",
                "springboot: hello ymj"
        );
    }
}
