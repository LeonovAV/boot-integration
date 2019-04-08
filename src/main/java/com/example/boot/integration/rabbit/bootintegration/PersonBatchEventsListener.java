package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PersonBatchEventsListener {

//    @RabbitListener(queues = {"person.batch.queue"}, containerFactory = "myContainerFactory")
    public void handlePayload(byte[] payload) {
        System.out.println(new String(payload));
    }

}
