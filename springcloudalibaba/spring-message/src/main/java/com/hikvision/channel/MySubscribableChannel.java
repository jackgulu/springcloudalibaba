package com.hikvision.channel;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Random;

public class MySubscribableChannel extends AbstractSubscribableChannel {
    private Random random = new Random();

    @Override
    protected boolean sendInternal(Message<?> message, long timeout) {
        if (message == null || CollectionUtils.isEmpty(getSubscribers())) {
            return false;
        }
        Iterator<MessageHandler> messageHandlerIterator = getSubscribers().iterator();
        int index = 0;
        int targetIndex = random.nextInt(getSubscribers().size());
        while (messageHandlerIterator.hasNext()) {
            MessageHandler messageHandler = messageHandlerIterator.next();
            if (index == targetIndex) {
                messageHandler.handleMessage(message);
                return true;
            }
            index++;
        }
        return false;
    }
}
