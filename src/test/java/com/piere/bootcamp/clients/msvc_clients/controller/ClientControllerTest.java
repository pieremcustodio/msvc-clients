package com.piere.bootcamp.clients.msvc_clients.controller;

import com.piere.bootcamp.clients.controller.ClientController;
import com.piere.bootcamp.clients.model.dto.BankResponse;
import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.msvc_clients.mock.MockClient;
import com.piere.bootcamp.clients.service.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest(ClientController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ClientController.class)
class ClientControllerTest {

    @Autowired
    private WebTestClient wtc;

    @MockBean
    private ClientService clientService;

    private final String BASE_URL = "/api/clients";

    @Test
    @DisplayName("Crear un cliente")
    void createClient() {
        ClientDto clientDto = MockClient.dto_01();
        BankResponse response = BankResponse.ok("Cliente creado correctamente", clientDto);

        Mockito.when(clientService.createClient(clientDto))
                .thenReturn(Mono.just(clientDto));

        wtc.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(clientService).createClient(clientDto);
    }

    @Test
    @DisplayName("Eliminar un cliente")
    void deleteClient() {
        ClientDto dto = MockClient.dto_01();

        Mockito.when(clientService.deleteClientById(dto.getId()))
                .thenReturn(Mono.empty());

        wtc.delete()
                .uri(BASE_URL + "/" + dto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals("Cliente eliminado correctamente", response.getMessage());
                });

        Mockito.verify(clientService).deleteClientById(dto.getId());
    }

    @Test
    @DisplayName("Obtener todos los clientes")
    void findAllClients() {
        List<ClientDto> clientList = MockClient.list_01();
        BankResponse response = BankResponse.ok("Listado de clientes", clientList);

        Mockito.when(clientService.findAllClients())
                .thenReturn(Flux.fromIterable(clientList));

        wtc.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertEquals(clientList.size(), ((List<?>) res.getData()).size());
                });

        Mockito.verify(clientService).findAllClients();
    }

    @Test
    @DisplayName("Buscar cliente por ID")
    void findClientById() {
        ClientDto clientDto = MockClient.dto_01();
        BankResponse response = BankResponse.ok("Obtener cliente por ID", clientDto);

        Mockito.when(clientService.findById(clientDto.getId()))
                .thenReturn(Mono.just(clientDto));

        wtc.get()
                .uri(BASE_URL + "/" + clientDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(clientService).findById(clientDto.getId());
    }

    @Test
    @DisplayName("Buscar cliente por número de documento")
    void findClientByDocumentNumber() {
        ClientDto clientDto = MockClient.dto_01();
        BankResponse response = BankResponse.ok("Obtener cliente por número de documento", clientDto);

        Mockito.when(clientService.findByDocumentNumber(clientDto.getDocumentNumber()))
                .thenReturn(Mono.just(clientDto));

        wtc.get()
                .uri(BASE_URL + "/findByDocumentNumber/" + clientDto.getDocumentNumber())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(clientService).findByDocumentNumber(clientDto.getDocumentNumber());
    }

    @Test
    @DisplayName("Actualizar un cliente")
    void updateClient() {
        ClientDto clientDto = MockClient.dto_01();
        BankResponse response = BankResponse.ok("Cliente actualizado correctamente", clientDto);

        Mockito.when(clientService.updateClient(clientDto))
                .thenReturn(Mono.just(clientDto));

        wtc.put()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(clientService).updateClient(clientDto);
    }
}
