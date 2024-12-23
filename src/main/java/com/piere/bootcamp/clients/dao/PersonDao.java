package com.piere.bootcamp.clients.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piere.bootcamp.clients.model.document.Person;

import reactor.core.publisher.Mono;

public interface PersonDao extends ReactiveMongoRepository<Person, String> {
    
    Mono<Person> findByDocumentNumber(String documentNumber);
}
