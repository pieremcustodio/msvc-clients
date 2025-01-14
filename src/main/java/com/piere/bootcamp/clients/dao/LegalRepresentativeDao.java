package com.piere.bootcamp.clients.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.piere.bootcamp.clients.model.document.LegalRepresentative;

import reactor.core.publisher.Mono;

public interface LegalRepresentativeDao extends ReactiveMongoRepository<LegalRepresentative, String> {

    Mono<LegalRepresentative> findByDocumentNumber(String documentNumber);
    
}
