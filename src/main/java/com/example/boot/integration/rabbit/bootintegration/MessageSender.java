package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplate rpcRabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;

    @Autowired
    private DirectExchange directExchange;

//    @Autowired
//    private BatchingRabbitTemplate batchingRabbitTemplate;

    public void send(String payload) {
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", payload);
    }

    public void sendAll(List<String> payload) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_BYTES);

//        payload.forEach(p -> {
//            batchingRabbitTemplate.send(fanoutExchange.getName(), "", new Message(p.getBytes(), messageProperties));
//        });
    }

    public String createTask(String personId) {
        Object received = rpcRabbitTemplate.convertSendAndReceive(directExchange.getName(), "rpc.reply.routing.key", personId);

        String result = "";
        if (received instanceof String) {
            result = (String) received;
        }

        return result;
    }

}
