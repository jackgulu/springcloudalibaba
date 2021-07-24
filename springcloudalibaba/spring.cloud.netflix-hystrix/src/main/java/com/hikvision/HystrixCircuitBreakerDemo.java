package com.hikvision;

import com.hikvision.command.CircuitBreakerRestCommand;
import com.hikvision.command.HelloWorldCommand;
import com.hikvision.command.TimeoutRestCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("raw")
@SpringBootApplication
public class HystrixCircuitBreakerDemo {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixCircuitBreakerDemo.class)
                .properties("spring.profiles.active=raw")
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
   @Bean
    CommandLineRunner commandLineRunner(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                HelloWorldCommand helloWorld1 = new HelloWorldCommand("200");
                HelloWorldCommand helloWorld2 = new HelloWorldCommand("500");
                System.err.println(
                        helloWorld1.execute() + " and Circuit Breaker is " + (helloWorld1.isCircuitBreakerOpen() ? "open"
                                : "closed"));
                System.err.println(
                        helloWorld2.execute() + " and Circuit Breaker is " + (helloWorld2.isCircuitBreakerOpen() ? "open"
                                : "closed"));
                System.err.println("================");

                int num = 1;
                while (num <= 10) {
                    TimeoutRestCommand command = new TimeoutRestCommand(num);
                    System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
                            command.isCircuitBreakerOpen() ? "open" : "closed"));
                    num++;
                }
                System.err.println("================");
                num = 1;
                while (num <= 15) {
                    CircuitBreakerRestCommand command = new CircuitBreakerRestCommand("500");
                    System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
                            command.isCircuitBreakerOpen() ? "open" : "closed"));
                    num++;
                }
                Thread.sleep(3000L);
                CircuitBreakerRestCommand command = new CircuitBreakerRestCommand("200");
                System.err.println("Execute " + num + ": " + command.execute() + " and Circuit Breaker is " + (
                        command.isCircuitBreakerOpen() ? "open" : "closed"));
            }

        };
   }


}
