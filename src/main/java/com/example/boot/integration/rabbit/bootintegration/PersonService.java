package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MessageSender messageSender;

    @Transactional(rollbackFor = RuntimeException.class)
    public void create() {
        Person p = new Person();
        p.setId(UUID.randomUUID().toString());
        p.setName("PapaCarlo");

        personRepository.save(p);

        // Publish event
        messageSender.send("PapaCarlo created");

//        throw new RuntimeException("Could not create person");
    }

}
