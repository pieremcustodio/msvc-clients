package com.piere.bootcamp.clients.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.LegalRepresentativeDao;
import com.piere.bootcamp.clients.model.document.LegalRepresentative;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.model.dto.PersonDto;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;
import com.piere.bootcamp.clients.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LegalRepresentativeServiceImpl implements LegalRepresentativeService {
    
    @Autowired
    private LegalRepresentativeDao legalRepresentativeDao;

    @Autowired
    private PersonService personService;

    @Override
    public Mono<LegalRepresentativeDto> createLegalRepresentative(LegalRepresentativeDto legalRepresentative) {
        return this.findByDocumentNumber(legalRepresentative.getPerson().getDocumentNumber())
                .flatMap(existingLegalRepresentative -> {
                    if (existingLegalRepresentative == null) {
                        Mono<PersonDto> person = personService.createPerson(legalRepresentative.getPerson());
                        return person.flatMap(p -> legalRepresentativeDao.save(LegalRepresentative.builder()
                                .personId(p.getId())
                                .status(legalRepresentative.getStatus())
                                .build()))
                                .map(LegalRepresentativeDto.build()::toDto);
                    } else {
                        return Mono.error(new IllegalArgumentException("Legal representative already exists"));
                    }
                });
    }

    @Override
    public Mono<LegalRepresentativeDto> updateLegalRepresentative(LegalRepresentativeDto legalRepresentative) {
        return personService.updatePerson(legalRepresentative.getPerson())
                .flatMap(person -> legalRepresentativeDao
                        .save(LegalRepresentativeDto.build().toEntity(legalRepresentative)))
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Mono<Void> deleteLegalRepresentative(LegalRepresentativeDto legalRepresentative) {
        return personService.deletePerson(legalRepresentative.getPerson())
                .then(legalRepresentativeDao.delete(LegalRepresentativeDto.build().toEntity(legalRepresentative)));

    }

    @Override
    public Flux<LegalRepresentativeDto> findAllLegalRepresentatives() {
        return legalRepresentativeDao.findAll()
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Mono<LegalRepresentativeDto> findByDocumentNumber(String documentNumber) {
        return personService.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.empty())
                .flatMap(person -> legalRepresentativeDao.findByPersonId(person.getId()))
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Flux<LegalRepresentativeDto> createLegalRepresentatives(List<LegalRepresentativeDto> legalRepresentatives) {
        return personService.createPersons(legalRepresentatives.stream()
                .map(LegalRepresentativeDto::getPerson)
                .collect(Collectors.toList()))
                .collectList()
                .flatMapMany(persons -> {
                    legalRepresentatives.forEach(legalRepresentative -> {
                        persons.stream()
                                .filter(person -> person.getDocumentNumber()
                                        .equals(legalRepresentative.getPerson().getDocumentNumber()))
                                .findFirst()
                                .ifPresent(legalRepresentative::setPerson);
                    });

                    List<LegalRepresentative> legalRepresentativeEntities = legalRepresentatives.stream()
                            .map(legalRepresentative -> LegalRepresentative.builder()
                                    .personId(legalRepresentative.getPerson().getId())
                                    .status(legalRepresentative.getStatus())
                                    .build())
                            .collect(Collectors.toList());
                    return legalRepresentativeDao.saveAll(legalRepresentativeEntities)
                            .map(LegalRepresentativeDto.build()::toDto);
                });
    };
}
