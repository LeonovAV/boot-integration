package com.example.boot.integration.rabbit.bootintegration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {

}
