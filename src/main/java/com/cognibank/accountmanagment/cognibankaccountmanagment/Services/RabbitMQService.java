package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;


@Configuration
public class RabbitMQService {

    @Autowired
    private Environment env;


    @Bean
    public Queue queue() {
        System.out.println("I am inside queue "+env.getProperty("spring.rabbitmq.api.queueName.lowbalance"));
        return QueueBuilder.durable(env.getProperty("spring.rabbitmq.api.queueName.lowbalance")).build();
    }

    @Bean
    public DirectExchange exchange() {
        System.out.println("I am inside exchange "+env.getProperty("spring.rabbitmq.api.directExchangeName"));
        return new DirectExchange(env.getProperty("spring.rabbitmq.api.directExchangeName"), true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        System.out.println("I am inside binding "+env.getProperty("spring.rabbitmq.api.routingKey.lowbalance"));
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("spring.rabbitmq.api.routingKey.lowbalance"));
    }


}
