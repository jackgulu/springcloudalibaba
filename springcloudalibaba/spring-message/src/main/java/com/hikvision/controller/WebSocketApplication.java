package com.hikvision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@SpringBootApplication
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
    }

    @Configuration
    @EnableWebSocketMessageBroker
    class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/topic/");
            registry.setApplicationDestinationPrefixes("/app");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/start").withSockJS();
        }
    }

    @Controller
    class WebSocketController {

        @Autowired
        private SimpMessageSendingOperations messagingTemplate;

        @MessageMapping("/subscribe")
        public void subscribe() {
            messagingTemplate.convertAndSend("/topic/tom", "jerry");
            messagingTemplate.convertAndSend("/topic/jerry", "tom");
        }

        @MessageMapping("/payload")
        public void payload(@Payload User user, @Header(value = "content-type") String contentType) {
            System.out.println("payload: " + user);
            System.out.println("header content-type: " + contentType);
        }

        @MessageMapping("/path/{var}")
        public void path(@DestinationVariable String var, Message message) {
            System.out.println("receive: " + message);
        }

        @MessageMapping("/message")
        public void message(String msg) {
            if (msg.contains("input1")) {
                messagingTemplate.convertAndSend("/topic/messages1", msg);
            } else if (msg.contains("input2")) {
                messagingTemplate.convertAndSend("/topic/messages2", msg);
            } else if (msg.contains("input3")) {
                messagingTemplate.convertAndSend("/topic/messages3", msg);
            } else {
                throw new IllegalStateException("unknown msg");
            }
        }

        @MessageExceptionHandler
        @SendTo("/topic/error")
        public String handleException(Throwable exception) {
            return exception.getMessage();
        }
    }

    static class User {

        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public User() {
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
