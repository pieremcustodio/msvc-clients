package com.piere.bootcamp.clients.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piere.bootcamp.clients.model.document.AuthorizedSignatory;

import reactor.core.publisher.Mono;

public interface AuthorizedSignatoryDao extends ReactiveMongoRepository<AuthorizedSignatory, String> {

    Mono<AuthorizedSignatory> findByPersonId(String id);
    
}
