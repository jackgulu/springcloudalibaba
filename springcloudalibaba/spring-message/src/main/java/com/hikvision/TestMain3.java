package com.hikvision;


import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;

public class TestMain3 {
    public static void main(String[] args) {
        //消息发送
        Properties kafkaProps=new Properties();
        kafkaProps.put("bootstrap.servers","localhost:9092");
        kafkaProps.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("partition.assignment.strategy","org.apache.kafka.clients.consumer.RangeAssignor");
        KafkaProducer<String,String> producer=new KafkaProducer<String, String>(kafkaProps);
        ProducerRecord<String,String> record=new ProducerRecord<String,String>("test","Apache Kafka message");
        producer.send(record);
        //消息接收
        Properties consumerProp=new Properties();
        consumerProp.put("bootstrap.servers","localhost:9092");
        consumerProp.put("group.id","my_group");
        consumerProp.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProp.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProp.put("partition.assignment.strategy","org.apache.kafka.clients.consumer.RangeAssignor");
        KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(consumerProp);
        consumer.subscribe("test");
        while (true){
            Set<Map.Entry<String, ConsumerRecords<String, String>>> entries = consumer.poll(100).entrySet();
            for (Map.Entry<String, ConsumerRecords<String, String>> d:entries){
             System.out.println(d.getKey()+":"+d.getValue());
            }
        }
    }
}
