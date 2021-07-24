package com.hikvision.channel;

import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyPollableChannel implements PollableChannel {

    private BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(1000);

    @Override
    public Message<?> receive() {
        return queue.poll();
    }

    @Override
    public Message<?> receive(long timeout) {
        try {
            return queue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean send(Message<?> message, long l) {
        return queue.add(message);
    }
}
