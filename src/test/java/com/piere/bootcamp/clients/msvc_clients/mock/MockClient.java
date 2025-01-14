package com.piere.bootcamp.clients.msvc_clients.mock;

import com.piere.bootcamp.clients.model.dto.ClientDto;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;
import com.piere.bootcamp.clients.model.enums.ProfileTypeEnum;
import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockClient {

    public static ClientDto dto_01() {
        return ClientDto.builder()
                .id("_1")
                .clientType(ClientTypeEnum.PERSONAL)
                .profileType(ProfileTypeEnum.NORMAL)
                .name("Peter")
                .lastName("Parker")
                .email("peter.parker@gmail.com")
                .address("Santa Anita 214")
                .cellphone("942142421")
                .phone("5942323")
                .documentType(DocumentTypeEnum.DNI)
                .documentNumber("12345678")
                .legalRepresentatives(Collections.emptyList())
                .authorizedSignatories(Collections.emptyList())
                .legalRepresentativeIds(Collections.emptyList())
                .authorizedSignatories(Collections.emptyList())
                .birthDate(LocalDate.of(1990, 1, 15))
                .status(true)
                .createAt(LocalDate.of(2022, 5, 1))
                .build();
    }

    public static ClientDto dto_02() {
        return ClientDto.builder()
                .id("_2")
                .clientType(ClientTypeEnum.EMPRESARIAL)
                .profileType(ProfileTypeEnum.PYME)
                .companyName("Stark Industries")
                .email("tony.stark@gmail.com")
                .address("Av. Industrial 456")
                .cellphone("987654321")
                .phone("4251212")
                .documentType(DocumentTypeEnum.RUC)
                .documentNumber("20512345678")
                .legalRepresentativeIds(Arrays.asList("_1", "_2"))
                .authorizedSignatoryIds(Arrays.asList("_1", "_2"))
                .legalRepresentatives(MockLegalRepresentative.list_01())
                .authorizedSignatories(MockAuthorizedSignatory.list_01())
                .status(true)
                .createAt(LocalDate.of(2021, 8, 10))
                .build();
    }

    public static List<ClientDto> list_01() {
        return Arrays.asList(
                dto_01(),
                dto_02()
        );
    }
}
