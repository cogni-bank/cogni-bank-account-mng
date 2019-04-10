package com.cognibank.accountmanagment.cognibankaccountmanagment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {

    @Autowired
    private Environment env;


    public Queue queue() {
        System.out.println("I am inside queue");
        return QueueBuilder.durable(env.getProperty("spring.rabbitmq.api.queueName.lowbalance")).build();
    }

    public DirectExchange exchange() {
        System.out.println("I am inside exchange");
        return new DirectExchange(env.getProperty("spring.rabbitmq.api.directExchangeName"), true, false);
    }

    public Binding binding(Queue queue, DirectExchange exchange) {
        System.out.println("I am inside binding");
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("spring.rabbitmq.api.routingKey.lowbalance"));
    }


    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        System.out.println("I am inside container");
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(env.getProperty("spring.rabbitmq.api.queueName.lowbalance"));
        //container.setMessageListener(listenerAdapter);
        return container;
    }
/*
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/


}
