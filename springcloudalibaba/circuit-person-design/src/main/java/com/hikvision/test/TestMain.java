package com.hikvision.test;

import com.hikvision.config.CircuitConfig;
import com.hikvision.dto.CircuitBreaker;
import com.hikvision.exception.DegradeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TestMain {
    public static void main(String[] args) {
        /*List<Integer> list= Arrays.asList(1,2,3,4,5);
        Consumer<Integer> consumer= System.out::println;
        list.stream().forEach(consumer);*/
        /*IntConsumer consumer=(x)->System.out.println(x);
        consumer.accept(23);*/
       /* Supplier<Double> supplier=()->new Random().nextDouble();
        System.out.println(supplier.get());
        Optional<Double> empty = Optional.of(20.23);
        empty.orElseGet(supplier);*/
       /* List<Integer> list= Arrays.asList(1,2,3,4,5,6,7,8);
        Predicate<Integer> predicate=(x)->x>5;
        List<Integer> collect = list.stream().filter(predicate).collect(Collectors.toList());
        collect.stream().forEach(x->System.out.println(x));*/

        CircuitBreaker circuitBreaker = new CircuitBreaker(new CircuitConfig());
        /*String bookName=circuitBreaker.run(()->{return "deep in spring cloud";},t->{return "boo ";});
        System.out.println(bookName);*/
       /* String result=circuitBreaker.run(()->{
            return 1/0+1+"";
        },t->{return "boom";});
      System.out.println(result);*/
        int degradeCount = 0;
        for (int index = 0; index < 10; index++) {
            String result = circuitBreaker.run(() -> {
                return 1 / 0 + 1 + "";
            }, t -> {
                if (t instanceof DegradeException) {
                    return "degrade";
                }
                return "boom";
            });
            if (result.equals("degrade")) {
                degradeCount++;
            }
        }

    }
}
