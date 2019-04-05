package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class RabbitConfiguration {

    private static final String exchangeName = "person.exchange";
    private static final String personQueueName = "person.queue";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Queue personQueue() {
        return new Queue(personQueueName);
    }

    @Bean
    public Binding personQueueBinding() {
        return BindingBuilder.bind(personQueue()).to(fanoutExchange());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    // From spring integration
    @Bean
    public MessageChannel messageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public AmqpInboundChannelAdapter inbound() {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(container(connectionFactory()));
        adapter.setOutputChannel(messageChannel());
        return adapter;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(personQueueName);
//        container.setConcurrentConsumers(2);
        return container;
    }

}
