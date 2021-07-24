package com.hikvision;

import com.hikvision.channel.MyPollableChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Collections;

public class TestPollMain {
    public static void main(String[] args) {
        MyPollableChannel channel=new MyPollableChannel();
        channel.send(MessageBuilder.withPayload("customer payload1").setHeader("k1","v1").build());
        channel.send(MessageBuilder.withPayload("customer payload2").setHeader("k1","v1").build());
        channel.send(MessageBuilder.createMessage("customer payload3",new MessageHeaders(Collections.singletonMap("ignore",true))));
        System.out.println(channel.receive());
        System.out.println(channel.receive());
        System.out.println(channel.receive());
        System.out.println(channel.receive());


    }
}
