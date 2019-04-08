package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    public void handlePersonCreatedMessage(String payload) {
        System.out.println(payload);
    }

}
