package com.piere.bootcamp.clients.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.ClientDao;
import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.document.Client;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;
import com.piere.bootcamp.clients.model.enums.TypeException;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
import com.piere.bootcamp.clients.service.ClientService;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private AuthorizedSignatoryService authorizedSignatoryService;

    @Autowired
    private LegalRepresentativeService legalRepresentativeService;

    @Override
    public Mono<ClientDto> createClient(ClientDto client) {
        return this.findByDocumentNumber(client.getDocumentNumber())
                .flatMap(existing -> Mono.<ClientDto>error(new BankException("El cliente ya existe", TypeException.E)))
                .switchIfEmpty(createNewClient(client));
    }

    @Override
    public Mono<Void> deleteClientById(String id) {
        return clientDao.findById(id)
                .switchIfEmpty(Mono.error(new BankException("El cliente no existe", TypeException.E)))
                .flatMap(clientDao::delete);
    }

    @Override
    public Mono<ClientDto> updateClient(ClientDto client) {
        return this.findByDocumentNumber(client.getDocumentNumber())
                .switchIfEmpty(Mono.error(new BankException("El cliente no existe", TypeException.E)))
                .flatMap(existing -> Mono.just(ClientDto.build().toEntity(client)))
                .flatMap(clientDao::save)
                .flatMap(savedClient -> Mono.just(ClientDto.build().toDto(savedClient)));
    }

    @Override
    public Flux<ClientDto> findAllClients() {
        return clientDao.findAll().flatMap(this::getData);
    }

    @Override
    public Flux<ClientDto> findAllByIdList(List<String> ids) {
        return clientDao.findAllById(ids).flatMap(this::getData);
    }

    @Override
    public Mono<ClientDto> findById(String id) {
        return clientDao.findById(id).flatMap(this::getData);
    }

    @Override
    public Mono<ClientDto> findByDocumentNumber(String documentNumber) {
        return clientDao.findByDocumentNumber(documentNumber).flatMap(this::getData);
    }

    private Mono<ClientDto> createNewClient(ClientDto client) {
        client.setStatus(true);
        client.setCreateAt(LocalDate.now());

        Client newClient = ClientDto.build().toEntity(client);

        if (client.getClientType() == ClientTypeEnum.EMPRESARIAL && (client.getLegalRepresentatives() == null || client.getLegalRepresentatives().isEmpty())) {
            return Mono.error(new BankException("Debe ingresar al menos un representante legal", TypeException.A));
        }
        
        if (client.getClientType() == ClientTypeEnum.PERSONAL) {
            return clientDao.save(newClient)
                    .map(ClientDto.build()::toDto);
        } else {
            Mono<List<LegalRepresentativeDto>> legalRepresentatives = createNewLegalRepresentatives(client);
            Mono<List<AuthorizedSignatoryDto>> authorizedSignatories = createNewAuthorizeSignatories(client);
    
            return Mono.zip(legalRepresentatives, authorizedSignatories)
                    .flatMap(tuple -> {
                        List<LegalRepresentativeDto> legalReps = tuple.getT1();
                        List<AuthorizedSignatoryDto> authSignatories = tuple.getT2();
                        newClient.setLegalRepresentativeIds(legalReps.stream()
                                .map(LegalRepresentativeDto::getId)
                                .collect(Collectors.toList()));
                        newClient.setAuthorizedSignatoryIds(authSignatories.stream()
                                .map(AuthorizedSignatoryDto::getId)
                                .collect(Collectors.toList()));
                        newClient.setCreateAt(LocalDate.now());
    
                        return clientDao.save(newClient)
                                .map(ClientDto.build()::toDto);
                    });
        }
    }

    private Mono<List<LegalRepresentativeDto>> createNewLegalRepresentatives(ClientDto client) {
        if (client.getClientType() == ClientTypeEnum.EMPRESARIAL) {
            return legalRepresentativeService.createLegalRepresentatives(client.getLegalRepresentatives())
                    .collectList();
        }
        return Mono.just(Collections.emptyList());
    }

    private Mono<List<AuthorizedSignatoryDto>> createNewAuthorizeSignatories(ClientDto client) {
        if (client.getClientType() == ClientTypeEnum.EMPRESARIAL) {
            return Mono.justOrEmpty(client.getAuthorizedSignatories())
                    .filter(list -> !list.isEmpty())
                    .flatMap(m -> authorizedSignatoryService.createAuthorizedSignatories(m).collectList());
        }
        return Mono.just(Collections.emptyList());
    }

    private Mono<ClientDto> getData(Client entity) {
        ClientDto dto = ClientDto.build().toDto(entity);

        if (dto.getClientType() == ClientTypeEnum.EMPRESARIAL) {
            Flux<AuthorizedSignatoryDto> authorizedSignatories = authorizedSignatoryService.findAllByIdList(dto.getAuthorizedSignatoryIds());
            Flux<LegalRepresentativeDto> legalRepresentatives = legalRepresentativeService.findAllByIdList(dto.getLegalRepresentativeIds());

            return Flux.zip(legalRepresentatives.collectList(), authorizedSignatories.collectList())
                    .map(tuple -> {
                        dto.setLegalRepresentatives(tuple.getT1());
                        dto.setAuthorizedSignatories(tuple.getT2());
                        return dto;
                    })
                    .next();
        } else {
            return Mono.just(dto);
        }
    }
}
