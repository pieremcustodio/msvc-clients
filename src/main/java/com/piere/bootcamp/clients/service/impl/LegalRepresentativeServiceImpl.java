package com.piere.bootcamp.clients.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.enums.TypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piere.bootcamp.clients.dao.LegalRepresentativeDao;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LegalRepresentativeServiceImpl implements LegalRepresentativeService {
    
    @Autowired
    private LegalRepresentativeDao legalRepresentativeDao;

    @Override
    public Mono<LegalRepresentativeDto> createLegalRepresentative(LegalRepresentativeDto legalRepresentative) {
        return this.findByDocumentNumber(legalRepresentative.getDocumentNumber())
                .flatMap(existing -> Mono.<LegalRepresentativeDto>error(new BankException("El representante legal ya existe", TypeException.E)))
                .switchIfEmpty(createNewLegalRepresentative(legalRepresentative));
    }

    @Override
    public Mono<LegalRepresentativeDto> updateLegalRepresentative(LegalRepresentativeDto legalRepresentative) {
        return this.findByDocumentNumber(legalRepresentative.getDocumentNumber())
                .switchIfEmpty(Mono.error(new BankException("El representante legal no existe", TypeException.E)))
                .flatMap(existing -> Mono.just(LegalRepresentativeDto.build().toEntity(legalRepresentative)))
                .flatMap(legalRepresentativeDao::save)
                .flatMap(save -> Mono.just(LegalRepresentativeDto.build().toDto(save)));
    }

    @Override
    public Mono<Void> deleteLegalRepresentativeById(String id) {
        return legalRepresentativeDao.findById(id)
                .switchIfEmpty(Mono.error(new BankException("El representante legal no existe", TypeException.E)))
                .flatMap(legalRepresentativeDao::delete);

    }

    @Override
    public Flux<LegalRepresentativeDto> findAllLegalRepresentatives() {
        return legalRepresentativeDao.findAll()
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Mono<LegalRepresentativeDto> findByDocumentNumber(String documentNumber) {
        return legalRepresentativeDao.findByDocumentNumber(documentNumber)
                .switchIfEmpty(Mono.empty())
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Flux<LegalRepresentativeDto> createLegalRepresentatives(List<LegalRepresentativeDto> legalRepresentatives) {
        return legalRepresentativeDao.saveAll(legalRepresentatives.stream()
                .map(LegalRepresentativeDto.build()::toEntity)
                .collect(Collectors.toList()))
                .map(LegalRepresentativeDto.build()::toDto);
    }

    @Override
    public Flux<LegalRepresentativeDto> findAllByIdList(List<String> idList) {
        return legalRepresentativeDao.findAllById(idList)
                .map(LegalRepresentativeDto.build()::toDto);
    }

    private Mono<LegalRepresentativeDto> createNewLegalRepresentative(LegalRepresentativeDto dto) {
        return legalRepresentativeDao.save(LegalRepresentativeDto.build().toEntity(dto))
                .map(LegalRepresentativeDto.build()::toDto);
    }
}
