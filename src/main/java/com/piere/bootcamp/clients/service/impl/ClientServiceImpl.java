package com.piere.bootcamp.clients.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.ClientDao;
import com.piere.bootcamp.clients.model.document.Client;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.model.dto.PersonDto;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
import com.piere.bootcamp.clients.service.ClientService;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;
import com.piere.bootcamp.clients.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private AuthorizedSignatoryService authorizedSignatoryService;

    @Autowired
    private PersonService personService;

    @Autowired
    private LegalRepresentativeService legalRepresentativeService;
    

    @Override
    public Mono<ClientDto> createClient(ClientDto client) {
        return this.findByDocumentNumber(client.getPerson().getDocumentNumber())
                .flatMap(existingClient -> Mono.error(new IllegalArgumentException("Client already exists")))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Mono<List<LegalRepresentativeDto>> legalRepresentatives = Mono.defer(() -> {
                                if (client.getClientType() == ClientTypeEnum.EMPRESARIAL) {
                                    return legalRepresentativeService
                                            .createLegalRepresentatives(client.getLegalRepresentatives())
                                            .collectList();
                                } else {
                                    return Mono.just(Collections.emptyList());
                                }
                            });

                            Mono<List<AuthorizedSignatoryDto>> authorizedSignatories = Mono.defer(() -> {
                                if (client.getClientType() == ClientTypeEnum.EMPRESARIAL) {
                                    return Mono.justOrEmpty(client.getAuthorizedSignatories())
                                            .filter(list -> !list.isEmpty())
                                            .flatMap(m -> authorizedSignatoryService
                                                    .createAuthorizedSignatories(m)
                                                    .collectList());
                                } else {
                                    return Mono.just(Collections.emptyList());
                                }
                            });

                            Mono<PersonDto> personMono = personService.createPerson(client.getPerson())
                                    .doOnNext(person -> client.setPerson(person));

                            return Mono.zip(legalRepresentatives, authorizedSignatories, personMono)
                                    .flatMap(tuple -> {
                                        List<LegalRepresentativeDto> legalReps = tuple.getT1();
                                        List<AuthorizedSignatoryDto> authSignatories = tuple.getT2();
                                        PersonDto person = tuple.getT3();

                                        Client newClient = Client.builder()
                                                .personId(person.getId())
                                                .legalRepresentativeIds(legalReps.stream()
                                                        .map(LegalRepresentativeDto::getId)
                                                        .collect(Collectors.toList()))
                                                .authorizedSignatoryIds(authSignatories.stream()
                                                        .map(AuthorizedSignatoryDto::getId)
                                                        .collect(Collectors.toList()))
                                                .createAt(LocalDate.now())
                                                .build();

                                        return clientDao.save(newClient)
                                                .map(ClientDto.build()::toDto);
                                    });
                        }))
                .cast(ClientDto.class)
                .onErrorResume(error -> {
                    System.err.println("Error: " + error.getMessage());
                    return Mono.error(error);
                });
    }

    @Override
    public Mono<Void> deleteClient(ClientDto client) {
        return clientDao.delete(ClientDto.build().toEntity(client));
    }

    @Override
    public Mono<ClientDto> updateClient(ClientDto client) {
        return personService.updatePerson(client.getPerson())
                .flatMap(person -> clientDao.save(ClientDto.build().toEntity(client))
                        .map(ClientDto.build()::toDto));
    }

    @Override
    public Flux<ClientDto> findAllClients() {
        return clientDao.findAll()
                .map(ClientDto.build()::toDto);
    }

    @Override
    public Flux<ClientDto> findAllByIdList(List<String> ids) {
        return clientDao.findAllById(ids)
                .map(ClientDto.build()::toDto);
    }

    @Override
    public Mono<ClientDto> findById(String id) {
        return clientDao.findById(id)
                .map(ClientDto.build()::toDto);
    }

    @Override
    public Mono<ClientDto> findByDocumentNumber(String documentNumber) {
        return personService.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.empty())
                .flatMap(person -> clientDao.findByPersonId(person.getId()))
                .map(ClientDto.build()::toDto);
    }
}
