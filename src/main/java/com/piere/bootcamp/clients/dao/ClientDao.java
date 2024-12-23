package com.piere.bootcamp.clients.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piere.bootcamp.clients.model.document.Client;

import reactor.core.publisher.Mono;


public interface ClientDao extends ReactiveMongoRepository<Client, String> {

    Mono<Client> findByPersonId(String id);

}
