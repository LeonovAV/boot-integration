package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    public void handlePersonCreatedMessage(String payload) {
        System.out.println(payload);
    }

}
