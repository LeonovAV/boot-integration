package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.BatchingStrategy;
import org.springframework.amqp.rabbit.core.support.SimpleBatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class RabbitConfiguration {

    private static final String exchangeName = "person.exchange";
    private static final String personQueueName = "person.queue";

    private static final String exchangeBatchName = "person.batch.exchange";
    private static final String personBatchQueueName = "person.batch.queue";

    private static final String rpcExchangeName = "rpc.exchange";
    private static final String rpcQueueName = "rpc.queue";
    private static final String rpcRoutingKey = "rpc.reply.routing.key";

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
    public FanoutExchange fanoutBatchExchange() {
        return new FanoutExchange(exchangeBatchName);
    }

    @Bean
    public Queue personBatchQueue() {
        return new Queue(personBatchQueueName);
    }

    @Bean
    public Binding personQueueBatchBinding() {
        return BindingBuilder.bind(personBatchQueue()).to(fanoutBatchExchange());
    }

    @Bean
    public Queue rpcQueue() { return new Queue(rpcQueueName); }

    @Bean
    public DirectExchange directExchange() { return new DirectExchange(rpcExchangeName); }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(rpcQueue()).to(directExchange()).with(rpcRoutingKey);
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

        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(20000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        rabbitTemplate.setRetryTemplate(retryTemplate);

        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate rpcRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());

        rabbitTemplate.setReplyTimeout(10000);

        return rabbitTemplate;
    }

    @Bean
    public BatchingRabbitTemplate batchingRabbitTemplate() {
        BatchingStrategy strategy = new SimpleBatchingStrategy(10, 100, 10000);
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        BatchingRabbitTemplate rabbitTemplate = new BatchingRabbitTemplate(strategy, scheduler);
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMissingQueuesFatal(false);
        return factory;
    }

    // From spring integration
//    @Bean
//    public MessageChannel messageChannel() {
//        return new PublishSubscribeChannel();
//    }
//
//    @Bean
//    public AmqpInboundChannelAdapter inbound() {
//        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(container(connectionFactory()));
//        adapter.setOutputChannel(messageChannel());
//        return adapter;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setQueueNames(personQueueName);
//        container.setMessageListener(new MessageListenerAdapter());
//        container.setConcurrentConsumers(2);
//        return container;
//    }

}
