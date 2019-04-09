package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PersonBatchEventsListener {

    private AtomicInteger ai = new AtomicInteger(0);

    @RabbitListener(queues = {"person.batch.queue"}, containerFactory = "myContainerFactory")
    public void handlePayload(String payload) {
        if (ai.incrementAndGet() == 5) {
            throw new RuntimeException("Stop processing");
        }
        System.out.println(payload);
    }

}
