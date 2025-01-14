package com.piere.bootcamp.clients.msvc_clients.controller;

import com.piere.bootcamp.clients.controller.LegalRepresentativeController;
import com.piere.bootcamp.clients.model.dto.LegalRepresentativeDto;
import com.piere.bootcamp.clients.model.dto.BankResponse;
import com.piere.bootcamp.clients.msvc_clients.mock.MockLegalRepresentative;
import com.piere.bootcamp.clients.service.LegalRepresentativeService;
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

@WebFluxTest(LegalRepresentativeController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = LegalRepresentativeController.class)
class LegalRepresentativeControllerTest {

    @Autowired
    private WebTestClient wtc;

    @MockBean
    private LegalRepresentativeService legalRepresentativeService;

    private final String BASE_URL = "/api/legalrepresentatives";

    @Test
    @DisplayName("Crear un representante legal")
    void createLegalRepresentative() {
        LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
        BankResponse response = BankResponse.ok("Representante legal creado correctamente", dto);

        Mockito.when(legalRepresentativeService.createLegalRepresentative(dto))
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

        Mockito.verify(legalRepresentativeService).createLegalRepresentative(dto);
    }

    @Test
    @DisplayName("Eliminar un representante legal")
    void deleteLegalRepresentative() {
        LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();

        Mockito.when(legalRepresentativeService.deleteLegalRepresentativeById(dto.getId()))
                .thenReturn(Mono.empty());

        wtc.delete()
                .uri(BASE_URL + "/" + dto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(response -> {
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals("Representante legal eliminado correctamente", response.getMessage());
                });

        Mockito.verify(legalRepresentativeService).deleteLegalRepresentativeById(dto.getId());
    }

    @Test
    @DisplayName("Listar todos los representantes legales")
    void findAllLegalRepresentatives() {
        List<LegalRepresentativeDto> representatives = MockLegalRepresentative.list_01();
        BankResponse response = BankResponse.ok("Lista de representantes legales", representatives);

        Mockito.when(legalRepresentativeService.findAllLegalRepresentatives())
                .thenReturn(Flux.fromIterable(representatives));

        wtc.get()
                .uri(BASE_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankResponse.class)
                .value(res -> {
                    Assertions.assertEquals(response.getMessage(), res.getMessage());
                    Assertions.assertEquals(representatives.size(), ((List<?>) res.getData()).size());
                });

        Mockito.verify(legalRepresentativeService).findAllLegalRepresentatives();
    }

    @Test
    @DisplayName("Actualizar un representante legal")
    void updateLegalRepresentative() {
        LegalRepresentativeDto dto = MockLegalRepresentative.dto_01();
        BankResponse response = BankResponse.ok("Representante legal actualizado correctamente", dto);

        Mockito.when(legalRepresentativeService.updateLegalRepresentative(dto))
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

        Mockito.verify(legalRepresentativeService).updateLegalRepresentative(dto);
    }
}
