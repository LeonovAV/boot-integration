package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    @RabbitListener(queues = {"rpc.queue"})
    public String createTask(String personId) {
        System.out.println("Task created for person " + personId);
        return UUID.randomUUID().toString();
    }

}
