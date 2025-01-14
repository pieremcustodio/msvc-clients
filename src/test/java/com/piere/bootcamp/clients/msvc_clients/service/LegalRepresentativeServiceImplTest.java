package com.piere.bootcamp.clients.msvc_clients.service;


import com.piere.bootcamp.clients.dao.LegalRepresentativeDao;
import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.document.LegalRepresentative;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.msvc_clients.mock.MockLegalRepresentative;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;
import com.piere.bootcamp.clients.service.impl.LegalRepresentativeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

@ContextConfiguration(classes = LegalRepresentativeService.class)
@ExtendWith(MockitoExtension.class)
class LegalRepresentativeServiceImplTest {
    
    @Mock
    private LegalRepresentativeDao legalRepresentativeDao;
    
    @InjectMocks
    private LegalRepresentativeServiceImpl legalRepresentativeService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para crear un representante legal")
    class createLegalRepresentative {

        @Test
        void legalRepresentativeAlreadyExists() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));
            Mockito.when(legalRepresentativeDao.save(Mockito.any()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));

            Mono<LegalRepresentativeDto> result = legalRepresentativeService.createLegalRepresentative(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El representante legal ya existe"))
                    .verify();

            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void createNewLegalRepresentative() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());
            Mockito.when(legalRepresentativeDao.save(Mockito.any()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));

            Mono<LegalRepresentativeDto> result = legalRepresentativeService.createLegalRepresentative(dto);

            StepVerifier.create(result)
                    .expectNextMatches(saved -> dto.getDocumentNumber().equals(saved.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(legalRepresentativeDao).save(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para actualizar un representante legal")
    class updateLegalRepresentative {

        @Test
        void legalRepresentativeDoesNotExist() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());

            Mono<LegalRepresentativeDto> result = legalRepresentativeService.updateLegalRepresentative(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El representante legal no existe"))
                    .verify();

            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void updateExistingLegalRepresentative() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));
            Mockito.when(legalRepresentativeDao.save(Mockito.any()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));

            Mono<LegalRepresentativeDto> result = legalRepresentativeService.updateLegalRepresentative(dto);

            StepVerifier.create(result)
                    .expectNextMatches(updated -> dto.getDocumentNumber().equals(updated.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(legalRepresentativeDao).save(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para eliminar un representante legal")
    class deleteLegalRepresentative {

        @Test
        void legalRepresentativeDoesNotExist() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findById(dto.getId()))
                    .thenReturn(Mono.empty());

            Mono<Void> result = legalRepresentativeService.deleteLegalRepresentativeById(dto.getId());

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El representante legal no existe"))
                    .verify();

            Mockito.verify(legalRepresentativeDao).findById(dto.getId());
        }

        @Test
        void deleteExistingLegalRepresentative() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findById(dto.getId()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));
            Mockito.when(legalRepresentativeDao.delete(Mockito.any()))
                    .thenReturn(Mono.empty());

            Mono<Void> result = legalRepresentativeService.deleteLegalRepresentativeById(dto.getId());

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(legalRepresentativeDao).findById(dto.getId());
            Mockito.verify(legalRepresentativeDao).delete(Mockito.any());
        }
    }

    @Test
    @DisplayName("Metodo para buscar todos los representantes legales")
    void findAllAuthorizedSignatories() {
        List<LegalRepresentativeDto> mockList = MockLegalRepresentative.list_01();
        Mockito.when(legalRepresentativeDao.findAll())
                .thenReturn(Flux.fromIterable(mockList).map(LegalRepresentativeDto.build()::toEntity));

        Flux<LegalRepresentativeDto> result = legalRepresentativeService.findAllLegalRepresentatives();

        StepVerifier.create(result)
                .expectNextSequence(mockList)
                .verifyComplete();

        Mockito.verify(legalRepresentativeDao).findAll();
    }

    @Test
    @DisplayName("Metodo para buscar todos los representantes legales por un listado de IDs")
    void findAllByIdList() {
        List<LegalRepresentativeDto> mockList = MockLegalRepresentative.list_01();
        List<String> idList = mockList.stream().map(LegalRepresentativeDto::getId).collect(Collectors.toList());
        Mockito.when(legalRepresentativeDao.findAllById(idList))
                .thenReturn(Flux.fromIterable(mockList).map(LegalRepresentativeDto.build()::toEntity));

        Flux<LegalRepresentativeDto> result = legalRepresentativeService.findAllByIdList(idList);

        StepVerifier.create(result)
                .expectNextSequence(mockList)
                .verifyComplete();

        Mockito.verify(legalRepresentativeDao).findAllById(idList);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Metodo para buscar un representante legal por su numero de documento")
    class findByDocumentNumber {
        @Test
        void legalRepresentativeAlreadyExist() {
            LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(LegalRepresentativeDto.build().toEntity(dto)));
            Mono<LegalRepresentativeDto> result = legalRepresentativeService.findByDocumentNumber(dto.getDocumentNumber());

            StepVerifier.create(result)
                    .expectNextMatches(test -> dto.getDocumentNumber().equals(test.getDocumentNumber()))
                    .verifyComplete();
            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void legalRepresentativeDoesNotExist() {
            String documentNumber = "12345678";
            Mockito.when(legalRepresentativeDao.findByDocumentNumber(documentNumber))
                    .thenReturn(Mono.empty());
            Mono<LegalRepresentativeDto> result = legalRepresentativeService.findByDocumentNumber(documentNumber);

            StepVerifier.create(result)
                    .verifyComplete();
            Mockito.verify(legalRepresentativeDao).findByDocumentNumber(documentNumber);
        }
    }

    @Test
    @DisplayName("Crear una lista de representantes legales")
    void createLegalRepresentatives() {
        List<LegalRepresentativeDto> dtos = MockLegalRepresentative.list_01();
        List<LegalRepresentative> entities = dtos.stream()
                .map(LegalRepresentativeDto.build()::toEntity)
                .collect(Collectors.toList());

        Mockito.when(legalRepresentativeDao.saveAll(entities))
                .thenReturn(Flux.fromIterable(entities));

        Flux<LegalRepresentativeDto> result = legalRepresentativeService.createLegalRepresentatives(dtos);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getDocumentNumber().equals("76895874"))
                .expectNextMatches(dto -> dto.getDocumentNumber().equals("76895524"))
                .verifyComplete();

        Mockito.verify(legalRepresentativeDao).saveAll(entities);
    }
}
