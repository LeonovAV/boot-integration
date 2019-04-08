package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = {"person.queue"})
public class PersonEventsListener {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PhotoService photoService;

    @RabbitHandler
    public void handlePersonCreatedMessage(String payload) {
        documentService.handlePersonCreatedMessage(payload);
        photoService.handlePersonCreatedMessage(payload);
    }

}
