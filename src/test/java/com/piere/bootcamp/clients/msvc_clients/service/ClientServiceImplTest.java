package com.piere.bootcamp.clients.msvc_clients.service;

import com.piere.bootcamp.clients.dao.ClientDao;
import com.piere.bootcamp.clients.exception.BankException;
import com.piere.bootcamp.clients.model.document.Client;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;
import com.piere.bootcamp.clients.msvc_clients.mock.MockAuthorizedSignatory;
import com.piere.bootcamp.clients.msvc_clients.mock.MockClient;
import com.piere.bootcamp.clients.msvc_clients.mock.MockLegalRepresentative;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
import com.piere.bootcamp.clients.service.ClientService;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;
import com.piere.bootcamp.clients.service.impl.ClientServiceImpl;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ContextConfiguration(classes = ClientService.class)
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientDao clientDao;

    @Mock
    private LegalRepresentativeService legalRepresentativeService;

    @Mock
    private AuthorizedSignatoryService authorizedSignatoryService;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para crear cliente")
    class createClient {

        @Test
        void clientAlreadyExists() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));

            Mono<ClientDto> result = clientService.createClient(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El cliente ya existe"))
                    .verify();

            Mockito.verify(clientDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void createEmpresarialClientWithoutRepresentatives() {
            ClientDto dto = MockClient.dto_02();
            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());
            dto.setLegalRepresentatives(Collections.emptyList());

            Mono<ClientDto> result = clientService.createClient(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("Debe ingresar al menos un representante legal"))
                    .verify();
        }

        @Test
        void createEmpresarialClientWithAllData() {
            ClientDto dto = MockClient.dto_02();

            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());
            Mockito.when(legalRepresentativeService.createLegalRepresentatives(dto.getLegalRepresentatives()))
                    .thenReturn(Flux.fromIterable(dto.getLegalRepresentatives()));
            Mockito.when(authorizedSignatoryService.createAuthorizedSignatories(dto.getAuthorizedSignatories()))
                    .thenReturn(Flux.fromIterable(dto.getAuthorizedSignatories()));
            Mockito.when(clientDao.save(Mockito.any(Client.class)))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));

            Mono<ClientDto> result = clientService.createClient(dto);

            StepVerifier.create(result)
                    .expectNextMatches(client -> dto.getDocumentNumber().equals(client.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(clientDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(clientDao).save(Mockito.any(Client.class));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para actualizar cliente")
    class updateClient {

        @Test
        void clientDoesNotExist() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.empty());

            Mono<ClientDto> result = clientService.updateClient(dto);

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El cliente no existe"))
                    .verify();

            Mockito.verify(clientDao).findByDocumentNumber(dto.getDocumentNumber());
        }

        @Test
        void updateExistingClient() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));
            Mockito.when(clientDao.save(Mockito.any()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));

            Mono<ClientDto> result = clientService.updateClient(dto);

            StepVerifier.create(result)
                    .expectNextMatches(updated -> dto.getDocumentNumber().equals(updated.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(clientDao).findByDocumentNumber(dto.getDocumentNumber());
            Mockito.verify(clientDao).save(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para eliminar cliente")
    class deleteClient {

        @Test
        void clientDoesNotExist() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findById(dto.getId())).thenReturn(Mono.empty());

            Mono<Void> result = clientService.deleteClientById(dto.getId());

            StepVerifier.create(result)
                    .expectErrorMatches(e -> e instanceof BankException &&
                            e.getMessage().equals("El cliente no existe"))
                    .verify();

            Mockito.verify(clientDao).findById(dto.getId());
        }

        @Test
        void deleteExistingClient() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findById(dto.getId()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));
            Mockito.when(clientDao.delete(Mockito.any()))
                    .thenReturn(Mono.empty());

            Mono<Void> result = clientService.deleteClientById(dto.getId());

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(clientDao).findById(dto.getId());
            Mockito.verify(clientDao).delete(Mockito.any());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para buscar cliente por ID")
    class findById {

        @Test
        void clientDoesNotExist() {
            String id = "123";
            Mockito.when(clientDao.findById(id)).thenReturn(Mono.empty());

            Mono<ClientDto> result = clientService.findById(id);

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(clientDao).findById(id);
        }

        @Test
        void clientExists() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findById(dto.getId()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));

            Mono<ClientDto> result = clientService.findById(dto.getId());

            StepVerifier.create(result)
                    .expectNextMatches(client -> client.getId().equals(dto.getId()) &&
                            client.getDocumentNumber().equals(dto.getDocumentNumber()))
                    .verifyComplete();

            Mockito.verify(clientDao).findById(dto.getId());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para buscar cliente por número de documento")
    class findByDocumentNumber {

        @Test
        void clientDoesNotExist() {
            String documentNumber = "76895874";
            Mockito.when(clientDao.findByDocumentNumber(documentNumber)).thenReturn(Mono.empty());

            Mono<ClientDto> result = clientService.findByDocumentNumber(documentNumber);

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(clientDao).findByDocumentNumber(documentNumber);
        }

        @Test
        void clientExists() {
            ClientDto dto = MockClient.dto_01();
            Mockito.when(clientDao.findByDocumentNumber(dto.getDocumentNumber()))
                    .thenReturn(Mono.just(ClientDto.build().toEntity(dto)));


            Mono<ClientDto> result = clientService.findByDocumentNumber(dto.getDocumentNumber());

            StepVerifier.create(result)
                    .expectNextMatches(client -> client.getDocumentNumber().equals(dto.getDocumentNumber()) &&
                            client.getName().equals(dto.getName()))
                    .verifyComplete();

            Mockito.verify(clientDao).findByDocumentNumber(dto.getDocumentNumber());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Método para buscar clientes por lista de IDs")
    class findAllByIdList {

        @Test
        void noClientsFoundForIds() {
            List<String> idList = List.of("123", "456");
            Mockito.when(clientDao.findAllById(idList)).thenReturn(Flux.empty());

            Flux<ClientDto> result = clientService.findAllByIdList(idList);

            StepVerifier.create(result)
                    .verifyComplete();

            Mockito.verify(clientDao).findAllById(idList);
        }

        @Test
        void clientsFoundForIds() {
            List<ClientDto> clients = MockClient.list_01();
            List<String> idList = clients.stream().map(ClientDto::getId).collect(Collectors.toList());

            Mockito.when(clientDao.findAllById(idList))
                    .thenReturn(Flux.fromIterable(clients).map(ClientDto.build()::toEntity));
            Mockito.when(authorizedSignatoryService.findAllByIdList(Mockito.anyList()))
                    .thenReturn(Flux.fromIterable(MockAuthorizedSignatory.list_01()));
            Mockito.when(legalRepresentativeService.findAllByIdList(Mockito.anyList()))
                    .thenReturn(Flux.fromIterable(MockLegalRepresentative.list_01()));

            Flux<ClientDto> result = clientService.findAllByIdList(idList);

            StepVerifier.create(result)
                    .expectNextMatches(client -> client.getId().equals("_1") &&
                            client.getClientType() == ClientTypeEnum.PERSONAL &&
                            client.getDocumentNumber().equals("12345678"))
                    .expectNextMatches(client -> client.getId().equals("_2") &&
                            client.getClientType() == ClientTypeEnum.EMPRESARIAL &&
                            client.getDocumentNumber().equals("20512345678"))
                    .verifyComplete();

            Mockito.verify(clientDao).findAllById(idList);
        }
    }

}
