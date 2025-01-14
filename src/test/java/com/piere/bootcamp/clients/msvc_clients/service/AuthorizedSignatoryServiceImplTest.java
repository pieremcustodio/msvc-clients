package com.piere.bootcamp.clients.msvc_clients.service;

import com.piere.bootcamp.clients.dao.AuthorizedSignatoryDao;
import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.document.AuthorizedSignatory;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.msvc_clients.mock.MockAuthorizedSignatory;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
import com.piere.bootcamp.clients.service.impl.AuthorizedSignatoryServiceImpl;
import org.junit.jupiter.api.*;
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


@ContextConfiguration(classes = AuthorizedSignatoryService.class)
@ExtendWith(MockitoExtension.class)
class AuthorizedSignatoryServiceImplTest {

    @Mock
    private AuthorizedSignatoryDao authorizedSignatoryDao;

    @InjectMocks
    private AuthorizedSignatoryServiceImpl authorizedSignatoryService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para crear un firmante autorizado")
    class createAuthorizedSignatory {

        @Test
        void authorizedSignatoryAlreadyExists() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));
            Mockito.when(authorizedSignatoryDao.save(Mockito.any()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));

            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.createAuthorizedSignatory(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El firmante autorizado ya existe"))
                    .verify();

            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void createNewAuthorizedSignatory() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());
            Mockito.when(authorizedSignatoryDao.save(Mockito.any()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));

            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.createAuthorizedSignatory(dto);

            StepVerifier.create(result)
                    .expectNextMatches(saved -> dto.getDocumentNumber().equals(saved.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(authorizedSignatoryDao).save(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para actualizar un firmante autorizado")
    class updateAuthorizedSignatory {

        @Test
        void authorizedSignatoryDoesNotExist() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());

            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.updateAuthorizedSignatory(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El firmante autorizado no existe"))
                    .verify();

            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void updateExistingAuthorizedSignatory() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));
            Mockito.when(authorizedSignatoryDao.save(Mockito.any()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));

            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.updateAuthorizedSignatory(dto);

            StepVerifier.create(result)
                    .expectNextMatches(updated -> dto.getDocumentNumber().equals(updated.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(authorizedSignatoryDao).save(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para eliminar un firmante autorizado")
    class deleteAuthorizedSignatory {

        @Test
        void authorizedSignatoryDoesNotExist() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findById(dto.getId()))
                    .thenReturn(Mono.empty());

            Mono<Void> result = authorizedSignatoryService.deleteAuthorizedSignatoryById(dto.getId());

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El representante legal no existe"))
                    .verify();

            Mockito.verify(authorizedSignatoryDao).findById(dto.getId());
        }

        @Test
        void deleteExistingAuthorizedSignatory() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findById(dto.getId()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));
            Mockito.when(authorizedSignatoryDao.delete(Mockito.any()))
                    .thenReturn(Mono.empty());

            Mono<Void> result = authorizedSignatoryService.deleteAuthorizedSignatoryById(dto.getId());

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(authorizedSignatoryDao).findById(dto.getId());
            Mockito.verify(authorizedSignatoryDao).delete(Mockito.any());
        }
    }

    @Test
    @DisplayName("Metodo para buscar todos los firmantes autorizados")
    void findAllAuthorizedSignatories() {
        List<AuthorizedSignatoryDto> mockList = MockAuthorizedSignatory.list_01();
        Mockito.when(authorizedSignatoryDao.findAll())
                .thenReturn(Flux.fromIterable(mockList).map(AuthorizedSignatoryDto.build()::toEntity));

        Flux<AuthorizedSignatoryDto> result = authorizedSignatoryService.findAllAuthorizedSignatories();

        StepVerifier.create(result)
                .expectNextSequence(mockList)
                .verifyComplete();

        Mockito.verify(authorizedSignatoryDao).findAll();
    }

    @Test
    @DisplayName("Metodo para buscar todos los firmantes autorizados por un listado de IDs")
    void findAllByIdList() {
        List<AuthorizedSignatoryDto> mockList = MockAuthorizedSignatory.list_01();
        List<String> idList = mockList.stream().map(AuthorizedSignatoryDto::getId).collect(Collectors.toList());
        Mockito.when(authorizedSignatoryDao.findAllById(idList))
                .thenReturn(Flux.fromIterable(mockList).map(AuthorizedSignatoryDto.build()::toEntity));

        Flux<AuthorizedSignatoryDto> result = authorizedSignatoryService.findAllByIdList(idList);

        StepVerifier.create(result)
                .expectNextSequence(mockList)
                .verifyComplete();

        Mockito.verify(authorizedSignatoryDao).findAllById(idList);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Metodo para buscar un firmante autorizado por su numero de documento")
    class findByDocumentNumber {
        @Test
        void authorizedSignatoryAlreadyExist() {
            AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(AuthorizedSignatoryDto.build().toEntity(dto)));
            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.findByDocumentNumber(dto.getDocumentNumber());

            StepVerifier.create(result)
                    .expectNextMatches(test -> dto.getDocumentNumber().equals(test.getDocumentNumber()))
                    .verifyComplete();
            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void authorizedSignatoryDoesNotExist() {
            String documentNumber = "12345678";
            Mockito.when(authorizedSignatoryDao.findByDocumentNumber(documentNumber))
                    .thenReturn(Mono.empty());
            Mono<AuthorizedSignatoryDto> result = authorizedSignatoryService.findByDocumentNumber(documentNumber);

            StepVerifier.create(result)
                    .verifyComplete();
            Mockito.verify(authorizedSignatoryDao).findByDocumentNumber(documentNumber);
        }
    }

    @Test
    @DisplayName("Crear una lista de firmantes autorizados")
    void createAuthorizedSignatories() {
        List<AuthorizedSignatoryDto> dtos = MockAuthorizedSignatory.list_01();
        List<AuthorizedSignatory> entities = dtos.stream()
                .map(AuthorizedSignatoryDto.build()::toEntity)
                .collect(Collectors.toList());

        Mockito.when(authorizedSignatoryDao.saveAll(entities))
                .thenReturn(Flux.fromIterable(entities));

        Flux<AuthorizedSignatoryDto> result = authorizedSignatoryService.createAuthorizedSignatories(dtos);

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getDocumentNumber().equals("76895874"))
                .expectNextMatches(dto -> dto.getDocumentNumber().equals("76895524"))
                .verifyComplete();

        Mockito.verify(authorizedSignatoryDao).saveAll(entities);
    }

}
