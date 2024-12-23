package com.piere.bootcamp.clients.service;

import com.piere.bootcamp.clients.model.dto.PersonDto;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {
    
    Mono<PersonDto> createPerson(PersonDto person);

    Mono<PersonDto> updatePerson(PersonDto person);

    Mono<Void> deletePerson(PersonDto person);

    Flux<PersonDto> findAllPersons();

    Mono<PersonDto> findByDocumentNumber(String documentNumber);

    Flux<PersonDto> createPersons(List<PersonDto> persons);
}
