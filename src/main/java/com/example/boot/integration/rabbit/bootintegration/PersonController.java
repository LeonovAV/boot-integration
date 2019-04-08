package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public void create() {
        personService.create();
    }

    @PostMapping
    @RequestMapping("/batch")
    public void createBatch() {
        personService.batchCreate();
    }

}
