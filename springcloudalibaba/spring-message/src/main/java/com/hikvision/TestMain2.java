package com.hikvision;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;

public class TestMain2 {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("rocketMq1");

        //设置nameserver地址：
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        //topic 消息将要发送的地址
        //body 具体消息内容
        Message message1 = new Message("myTopic001","rocketMq1 第一次发送".getBytes());
        Message message2 = new Message("myTopic001","rocketMq1 第二次发送".getBytes());
        Message message3 = new Message("myTopic001","rocketMq1 第三次发送".getBytes());

        ArrayList arrayList = new ArrayList();
        arrayList.add(message1);
        arrayList.add(message2);
        arrayList.add(message3);

        SendResult result = producer.send(arrayList);

        System.out.println("发送结果 ："+result);
        producer.shutdown();
        System.out.println("生产者下线！");
    }
}
