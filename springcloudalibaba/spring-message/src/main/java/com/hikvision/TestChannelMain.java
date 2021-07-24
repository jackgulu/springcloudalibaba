package com.hikvision;

import com.hikvision.channel.MySubscribableChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class TestChannelMain {
    public static void main(String[] args) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        MySubscribableChannel channel = new MySubscribableChannel();
        channel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                String ignoreKey = "ignore";
                if (message.getHeaders().containsKey(ignoreKey) && message.getHeaders().get(ignoreKey, Boolean.class)) {
                    return null;
                }
                return message;
            }

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                if (sent) {
                    successCount.incrementAndGet();
                } else {
                    failCount.incrementAndGet();
                }
            }
        });
        channel.send(MessageBuilder.withPayload("customer payload1").setHeader("k1", "v1").build());
        channel.subscribe(msg -> {
            System.out.println("[" + Thread.currentThread().getName() + "] handler1 receive:" + msg);
        });

        channel.subscribe(msg -> {
            System.out.println("[" + Thread.currentThread().getName() + "] handler2 receive:" + msg);
        });
        channel.subscribe(msg -> {
            System.out.println("[" + Thread.currentThread().getName() + "] handler3 receive:" + msg);
        });
        channel.send(MessageBuilder.withPayload("customer payload2").setHeader("k2","v2").build());
        channel.send(MessageBuilder.createMessage("customer payload2",new MessageHeaders(Collections.singletonMap("ignore",true))));
        System.out.println("successCount: "+successCount.get()+", failCount:"+failCount.get());
    }
}
