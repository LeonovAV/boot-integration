package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;

    public void send(String payload) {
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", payload);
    }

}
