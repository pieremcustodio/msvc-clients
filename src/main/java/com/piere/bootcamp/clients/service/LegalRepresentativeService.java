package com.piere.bootcamp.clients.service;

import java.util.List;

import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LegalRepresentativeService {
    
    Mono<LegalRepresentativeDto> createLegalRepresentative(LegalRepresentativeDto legalRepresentative);

    Mono<LegalRepresentativeDto> updateLegalRepresentative(LegalRepresentativeDto legalRepresentative);

    Mono<Void> deleteLegalRepresentativeById(String id);

    Mono<LegalRepresentativeDto> findByDocumentNumber(String documentNumber);

    Flux<LegalRepresentativeDto> findAllLegalRepresentatives();

    Flux<LegalRepresentativeDto> createLegalRepresentatives(List<LegalRepresentativeDto> legalRepresentatives);

    Flux<LegalRepresentativeDto> findAllByIdList(List<String> idList);

}
