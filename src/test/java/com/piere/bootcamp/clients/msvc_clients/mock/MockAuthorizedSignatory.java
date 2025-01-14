package com.piere.bootcamp.clients.msvc_clients.mock;

import com.piere.bootcamp.clients.model.dto.AuthorizedSignatoryDto;
import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;

import java.util.Arrays;
import java.util.List;

public class MockAuthorizedSignatory {

    public static AuthorizedSignatoryDto dto_01() {
        return AuthorizedSignatoryDto.builder()
                .id("_1")
                .name("John")
                .lastName("Carter")
                .email("john.carter@gmail.com")
                .address("Los Olivos 1530")
                .cellphone("942943444")
                .phone("5347654")
                .documentType(DocumentTypeEnum.DNI)
                .documentNumber("76895874")
                .status(true).build();
    }

    public static List<AuthorizedSignatoryDto> list_01() {
        return Arrays.asList(
                AuthorizedSignatoryDto.builder()
                        .id("_1")
                        .name("John")
                        .lastName("Carter")
                        .email("john.carter@gmail.com")
                        .address("Los Olivos 1530")
                        .cellphone("942943444")
                        .phone("5347654")
                        .documentType(DocumentTypeEnum.DNI)
                        .documentNumber("76895874")
                        .status(true).build(),
                AuthorizedSignatoryDto.builder()
                        .id("_2")
                        .name("Tony")
                        .lastName("Stark")
                        .email("tony.stark@gmail.com")
                        .address("Santa Anita 1530")
                        .cellphone("942943422")
                        .phone("5347624")
                        .documentType(DocumentTypeEnum.DNI)
                        .documentNumber("76895524")
                        .status(true).build()
        );
    }
}
