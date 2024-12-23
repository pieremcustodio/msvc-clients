package com.piere.bootcamp.clients.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.PersonDao;
import com.piere.bootcamp.clients.model.dto.PersonDto;
import com.piere.bootcamp.clients.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonServiceImpl implements PersonService {
    
    @Autowired
    private PersonDao personDao;

    @Override
    public Mono<PersonDto> createPerson(PersonDto person) {
        return personDao.save(PersonDto.build().toEntity(person))
                .map(PersonDto.build()::toDto);
    }

    @Override
    public Mono<PersonDto> updatePerson(PersonDto person) {
        return personDao.findByDocumentNumber(person.getDocumentNumber())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Persona no encontrada")))
                .flatMap(p -> personDao.save(PersonDto.build().toEntity(person)))
                .map(PersonDto.build()::toDto);
    }

    @Override
    public Mono<Void> deletePerson(PersonDto person) {
        return personDao.findByDocumentNumber(person.getDocumentNumber())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Persona no encontrada")))
                .flatMap(p -> personDao.delete(p));
    }

    @Override
    public Flux<PersonDto> findAllPersons() {
        return personDao.findAll()
                .map(PersonDto.build()::toDto);
    }

    @Override
    public Mono<PersonDto> findByDocumentNumber(String documentNumber) {
        return personDao.findByDocumentNumber(documentNumber)
                .map(PersonDto.build()::toDto);
    }

    @Override
    public Flux<PersonDto> createPersons(List<PersonDto> persons) {
        return personDao.saveAll(persons.stream()
                .map(PersonDto.build()::toEntity)
                .collect(Collectors.toList()))
                .map(PersonDto.build()::toDto);
    }  
}
