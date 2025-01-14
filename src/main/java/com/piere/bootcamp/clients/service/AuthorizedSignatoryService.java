package com.piere.bootcamp.clients.service;

import java.util.List;

import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorizedSignatoryService {
    
    Mono<AuthorizedSignatoryDto> createAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory);

    Mono<AuthorizedSignatoryDto> updateAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory);

    Mono<Void> deleteAuthorizedSignatoryById(String id);

    Mono<AuthorizedSignatoryDto> findByDocumentNumber(String documentNumber);

    Flux<AuthorizedSignatoryDto> findAllAuthorizedSignatories();

    Flux<AuthorizedSignatoryDto> createAuthorizedSignatories(List<AuthorizedSignatoryDto> authorizedSignatories);

    Flux<AuthorizedSignatoryDto> findAllByIdList(List<String> idList);
}
