package com.piere.bootcamp.clients.service;

import java.util.List;

import com.piere.bootcamp.clients.model.dto.ClientDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    
    Mono<ClientDto> createClient(ClientDto client);

    Mono<Void> deleteClient(ClientDto client);

    Mono<ClientDto> updateClient(ClientDto client);

    Flux<ClientDto> findAllClients();
    
    Flux<ClientDto> findAllByIdList(List<String> ids);

    Mono<ClientDto> findById(String id);

    Mono<ClientDto> findByDocumentNumber(String documentNumber);
}
