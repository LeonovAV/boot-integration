package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @ServiceActivator(inputChannel = "messageChannel")
    public void handlePersonCreatedMessage(String payload) {
        System.out.println(payload);
    }

}
