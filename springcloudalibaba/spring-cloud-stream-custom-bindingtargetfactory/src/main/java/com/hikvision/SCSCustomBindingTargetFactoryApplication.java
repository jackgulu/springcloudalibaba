package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.AbstractBindingTargetFactory;
import org.springframework.cloud.stream.binding.BindingTargetFactory;
import org.springframework.cloud.stream.binding.CompositeMessageChannelConfigurer;
import org.springframework.cloud.stream.binding.MessageChannelConfigurer;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableBinding({SCSCustomBindingTargetFactoryApplication.MySink.class})
public class SCSCustomBindingTargetFactoryApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSCustomBindingTargetFactoryApplication.class)
                .web(WebApplicationType.NONE).run(args);
    }

    public interface MySink {
        String INPUT = "input";

        @Input(INPUT)
        PublishSubscribeChannel input();
    }

    class PublishSubscribeChannelBindingTargetFactory extends AbstractBindingTargetFactory<PublishSubscribeChannel> {


        private MessageChannelConfigurer channelConfigurer;
        @Autowired
        private GenericApplicationContext context;

        public PublishSubscribeChannelBindingTargetFactory(MessageChannelConfigurer messageChannelConfigurer) {
            super(PublishSubscribeChannel.class);
            this.channelConfigurer = messageChannelConfigurer;

        }

        @Override
        public PublishSubscribeChannel createInput(String name) {
            PublishSubscribeChannel channel = new PublishSubscribeChannel();
            channel.setComponentName(name);
            this.channelConfigurer.configureInputChannel(channel, name);
            if (context != null && !context.containsBean(name)) {
                context.registerBean(name, PublishSubscribeChannel.class, () -> channel);
            }
            return channel;
        }

        @Override
        public PublishSubscribeChannel createOutput(String name) {
            PublishSubscribeChannel channel = new PublishSubscribeChannel();
            channel.setComponentName(name);
            this.channelConfigurer.configureOutputChannel(channel, name);
            if (context != null && !context.containsBean(name)) {
                context.registerBean(name, PublishSubscribeChannel.class, () -> channel);
            }
            return channel;
        }
    }

    @Bean
    @Primary
    BindingTargetFactory publishSubscribeChannelBindingTargetFactory(
            CompositeMessageChannelConfigurer compositeMessageChannelConfigurer
    ) {
        return new PublishSubscribeChannelBindingTargetFactory(compositeMessageChannelConfigurer);
    }
    @Service
    class MySinkService{
        @StreamListener(Sink.INPUT)
        public void receive1(String receiveMsg) {
            System.out.println("receive1: " + receiveMsg);
        }

        @StreamListener(Sink.INPUT)
        public void receive2(String receiveMsg) {
            System.out.println("receive2: " + receiveMsg);
        }

    }

    @Autowired
    private MySink sink;

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            sink.input().send(MessageBuilder.withPayload("send by application with memory, not real MQ").build());
        };
    }
}
