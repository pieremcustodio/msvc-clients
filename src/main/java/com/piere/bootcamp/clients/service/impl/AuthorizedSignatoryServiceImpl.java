package com.piere.bootcamp.clients.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.AuthorizedSignatoryDao;
import com.piere.bootcamp.clients.model.document.AuthorizedSignatory;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
import com.piere.bootcamp.clients.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorizedSignatoryServiceImpl implements AuthorizedSignatoryService {
    
    @Autowired
    private AuthorizedSignatoryDao authorizedSignatoryDao;

    @Autowired
    private PersonService personService;

    @Override
    public Mono<AuthorizedSignatoryDto> createAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory) {
        return this.findByDocumentNumber(authorizedSignatory.getPerson().getDocumentNumber())
                .flatMap(existingAuthorizedSignatory -> Mono.error(new IllegalArgumentException("Authorized signatory already exists")))
                .switchIfEmpty(
                    personService.createPerson(authorizedSignatory.getPerson())
                        .flatMap(p -> authorizedSignatoryDao.save(AuthorizedSignatory.builder()
                                .personId(p.getId())
                                .status(authorizedSignatory.getStatus())
                                .build()))
                        .map(AuthorizedSignatoryDto.build()::toDto)
                )
                .cast(AuthorizedSignatoryDto.class);
    }

    @Override
    public Mono<AuthorizedSignatoryDto> updateAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory) {
        return personService.updatePerson(authorizedSignatory.getPerson())
                .flatMap(person -> authorizedSignatoryDao
                        .save(AuthorizedSignatoryDto.build().toEntity(authorizedSignatory)))
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Mono<Void> deleteAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory) {
        return personService.deletePerson(authorizedSignatory.getPerson())
                .then(authorizedSignatoryDao.delete(AuthorizedSignatoryDto.build().toEntity(authorizedSignatory)));
    }

    @Override
    public Flux<AuthorizedSignatoryDto> findAllAuthorizedSignatories() {
        return authorizedSignatoryDao.findAll()
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Mono<AuthorizedSignatoryDto> findByDocumentNumber(String documentNumber) {
        return personService.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.empty())
                .flatMap(person -> authorizedSignatoryDao.findByPersonId(person.getId()))
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Flux<AuthorizedSignatoryDto> createAuthorizedSignatories(
            List<AuthorizedSignatoryDto> authorizedSignatories) {
        return personService.createPersons(authorizedSignatories.stream()
                .map(AuthorizedSignatoryDto::getPerson)
                .collect(Collectors.toList()))
                .collectList()
                .flatMapMany(persons -> {
                    authorizedSignatories.forEach(authSignatory -> {
                        persons.stream()
                                .filter(person -> person.getDocumentNumber()
                                        .equals(authSignatory.getPerson().getDocumentNumber()))
                                .findFirst()
                                .ifPresent(authSignatory::setPerson);
                    });
                    List<AuthorizedSignatory> authorizedSignatoriesEntities = authorizedSignatories.stream()
                            .map(authorizedSignatory -> AuthorizedSignatory.builder()
                                .personId(authorizedSignatory.getPerson().getId())
                                    .status(authorizedSignatory.getStatus())
                                    .build())
                            .collect(Collectors.toList());
                    return authorizedSignatoryDao.saveAll(authorizedSignatoriesEntities)
                            .map(AuthorizedSignatoryDto.build()::toDto);
                });
    }
}
