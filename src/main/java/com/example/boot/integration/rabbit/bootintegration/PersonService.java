package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
        String personId = UUID.randomUUID().toString();
        p.setId(personId);
        p.setName("PapaCarlo");

        personRepository.save(p);

        // Create task
        String taskId = messageSender.createTask(personId);

        System.out.println("Received task id for person " + taskId);

        // Publish event
        messageSender.send("PapaCarlo created");

//        throw new RuntimeException("Could not create person");
    }

    @Transactional
    public void batchCreate() {
        List<String> payload = Arrays.asList("a", "b", "c", "d", "e", "f", "a", "b", "c", "d", "e", "f", "a", "b", "c", "d", "e", "f");
        messageSender.sendAll(payload);
    }

}
