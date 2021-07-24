package com.hikvision;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class TestMain {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //消息发送
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup_name");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        Message message = new Message("me_topic", "Apache RocketMQ message".getBytes());
        producer.send(message, new SendCallback() {
            public void onSuccess(SendResult sendResult) {

                System.out.println("发送成功！");
                System.out.println("发送结果 ：" + sendResult);
            }

            public void onException(Throwable throwable) {

                throwable.printStackTrace();
                System.out.println("发送失败！");
            }
        });
        //消费消息
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup_name");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("me_topic", "*");
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            MessageExt messageExt = msgs.get(0);
            try {
                System.out.println(new String(messageExt.getBody(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
    }
}
