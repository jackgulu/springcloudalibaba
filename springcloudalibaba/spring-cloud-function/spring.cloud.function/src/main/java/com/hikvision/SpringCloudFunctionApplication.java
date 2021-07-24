package com.hikvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionRegistry;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringCloudFunctionApplication {
    private Random random = new Random();
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringCloudFunctionApplication.class, args);
        Object apply = applicationContext.getBean("upperCase", Function.class).apply("sdsd");
        System.out.println(apply);
        FunctionRegistry functionRegistry = applicationContext.getBean(FunctionRegistry.class);
        Function<String, String> upperCaseFunc = functionRegistry.lookup(Function.class, "upperCase");
        upperCaseFunc.apply("b"); // B
        Function<String, String> lowerCaseFunc = functionRegistry.lookup(Function.class, "lowerCase");
        System.out.println(lowerCaseFunc.apply("B")); // b
        System.out.println(functionRegistry.getNames(Function.class));
        System.out.println(functionRegistry.getNames(Consumer.class));
        Consumer<String> consumer = code -> {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity("http://httpbin.org/status/" + code, String.class);
            System.out.println(responseEntity.getStatusCode());
        };
        functionRegistry.register(new FunctionRegistration(consumer, "httpFunc").type(FunctionType.of(Consumer.class)));
        System.out.println(applicationContext.getBeansOfType(Function.class));
        Consumer<String> httpFunc = functionRegistry.lookup(Consumer.class, "httpFunc");
        httpFunc.accept("200");
        Function routingFunction = functionRegistry.lookup(Function.class, RoutingFunction.FUNCTION_NAME);
        routingFunction.apply("routing"); // ROUTING

        Function composite = functionRegistry.lookup("upperCase|print");
        composite.apply("hello function"); // HELLO FUNCTION
    }

    @Bean    public Function<String, String> upperCase() {

        return s -> s.toUpperCase();
    }

    @Bean
    public Function<String, String> lowerCase() {
        return s -> s.toLowerCase();
    }

    @Bean
    public Supplier<Integer> random() {
        return () -> random.nextInt(1000);
    }

    @Bean
    public Consumer<String> consumer() {
        return code -> {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://httpbin.org/status" + code, String.class);
            System.out.println(responseEntity.getStatusCode().toString());
        };
    }
}
