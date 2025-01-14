package com.piere.bootcamp.clients.msvc_clients.controller;

import com.piere.bootcamp.clients.controller.AuthorizedSignatoryController;
import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.model.dto.BankResponse;
import com.piere.bootcamp.clients.msvc_clients.mock.MockAuthorizedSignatory;
import com.piere.bootcamp.clients.service.AuthorizedSignatoryService;
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

@WebFluxTest(AuthorizedSignatoryController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = AuthorizedSignatoryController.class)
class AuthorizedSignatoryControllerTest {

    @Autowired
    private WebTestClient wtc;

    @MockBean
    private AuthorizedSignatoryService authorizedSignatoryService;

    private final String BASE_URL = "/api/authorizedsignatories";

    @Test
    @DisplayName("Crear un firmante autorizado")
    void createAuthorizedSignatory() {
        AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
        BankResponse response = BankResponse.ok("Firmante autorizado creado correctamente", dto);

        Mockito.when(authorizedSignatoryService.createAuthorizedSignatory(dto))
                .thenReturn(Mono.just(dto));

        wtc.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(authorizedSignatoryService).createAuthorizedSignatory(dto);
    }

    @Test
    @DisplayName("Eliminar un firmante autorizado")
    void deleteAuthorizedSignatory() {
        AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();

        Mockito.when(authorizedSignatoryService.deleteAuthorizedSignatoryById(dto.getId()))
                .thenReturn(Mono.empty());

        wtc.delete()
                .uri(BASE_URL + "/" + dto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals("Firmante autorizado eliminado correctamente", response.getMessage());
                });

        Mockito.verify(authorizedSignatoryService).deleteAuthorizedSignatoryById(dto.getId());
    }

    @Test
    @DisplayName("Listar todos los firmantes autorizados")
    void findAllAuthorizedSignatories() {
        List<AuthorizedSignatoryDto> signatories = MockAuthorizedSignatory.list_01();
        BankResponse response = BankResponse.ok("Lista de firmantes autorizados", signatories);

        Mockito.when(authorizedSignatoryService.findAllAuthorizedSignatories())
                .thenReturn(Flux.fromIterable(signatories));

        wtc.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertEquals(signatories.size(), ((List<?>) res.getData()).size());
                });

        Mockito.verify(authorizedSignatoryService).findAllAuthorizedSignatories();
    }

    @Test
    @DisplayName("Actualizar un firmante autorizado")
    void updateAuthorizedSignatory() {
        AuthorizedSignatoryDto dto = MockAuthorizedSignatory.dto_01();
        BankResponse response = BankResponse.ok("Firmante autorizado actualizado correctamente", dto);

        Mockito.when(authorizedSignatoryService.updateAuthorizedSignatory(dto))
                .thenReturn(Mono.just(dto));

        wtc.put()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertNotNull(res.getData());
                });

        Mockito.verify(authorizedSignatoryService).updateAuthorizedSignatory(dto);
    }
}
