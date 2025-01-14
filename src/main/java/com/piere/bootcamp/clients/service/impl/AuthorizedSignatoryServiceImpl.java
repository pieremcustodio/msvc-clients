package com.piere.bootcamp.clients.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.enums.TypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.AuthorizedSignatoryDao;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorizedSignatoryServiceImpl implements AuthorizedSignatoryService {
    
    @Autowired
    private AuthorizedSignatoryDao authorizedSignatoryDao;

    @Override
    public Mono<AuthorizedSignatoryDto> createAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory) {
        return this.findByDocumentNumber(authorizedSignatory.getDocumentNumber())
                .flatMap(existing -> Mono.<AuthorizedSignatoryDto>error(new BankException("El firmante autorizado ya existe", TypeException.E)))
                .switchIfEmpty(createNewAuthorizedSignatory(authorizedSignatory));
    }

    @Override
    public Mono<AuthorizedSignatoryDto> updateAuthorizedSignatory(AuthorizedSignatoryDto authorizedSignatory) {
        return this.findByDocumentNumber(authorizedSignatory.getDocumentNumber())
                .switchIfEmpty(Mono.error(new BankException("El firmante autorizado no existe", TypeException.E)))
                .flatMap(existing -> Mono.just(AuthorizedSignatoryDto.build().toEntity(authorizedSignatory)))
                .flatMap(authorizedSignatoryDao::save)
                .flatMap(save -> Mono.just(AuthorizedSignatoryDto.build().toDto(save)));
    }

    @Override
    public Mono<Void> deleteAuthorizedSignatoryById(String id) {
        return authorizedSignatoryDao.findById(id)
                .switchIfEmpty(Mono.error(new BankException("El representante legal no existe", TypeException.E)))
                .flatMap(authorizedSignatoryDao::delete);
    }

    @Override
    public Flux<AuthorizedSignatoryDto> findAllAuthorizedSignatories() {
        return authorizedSignatoryDao.findAll()
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Mono<AuthorizedSignatoryDto> findByDocumentNumber(String documentNumber) {
        return authorizedSignatoryDao.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.empty())
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Flux<AuthorizedSignatoryDto> createAuthorizedSignatories(
            List<AuthorizedSignatoryDto> authorizedSignatories) {
        return authorizedSignatoryDao.saveAll(authorizedSignatories.stream()
                .map(AuthorizedSignatoryDto.build()::toEntity)
                .collect(Collectors.toList()))
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    @Override
    public Flux<AuthorizedSignatoryDto> findAllByIdList(List<String> idList) {
        return authorizedSignatoryDao.findAllById(idList)
                .map(AuthorizedSignatoryDto.build()::toDto);
    }

    private Mono<AuthorizedSignatoryDto> createNewAuthorizedSignatory(AuthorizedSignatoryDto dto) {
        return authorizedSignatoryDao.save(AuthorizedSignatoryDto.build().toEntity(dto))
                .map(AuthorizedSignatoryDto.build()::toDto);
    }
}
