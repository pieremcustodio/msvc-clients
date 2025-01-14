package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.piere.bootcamp.clients.model.document.Client;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;

import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;
import com.piere.bootcamp.clients.model.enums.ProfileTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ClientDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private ClientTypeEnum clientType;

    private ProfileTypeEnum profileType;

    private String name;

    private String lastName;

    private String companyName;

    private String email;

    private String address;

    private String cellphone;

    private String phone;

    private DocumentTypeEnum documentType;

    private String documentNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    private List<String> legalRepresentativeIds;

    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Builder.Default
    private List<LegalRepresentativeDto> legalRepresentatives = null;

    private List<String> authorizedSignatoryIds;

    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Builder.Default
    private List<AuthorizedSignatoryDto> authorizedSignatories = null;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createAt;

    private Boolean status;

    public static ClientDto build() {
        return ClientDto.builder().build();
    }

    public ClientDto toDto(Client entity) {
        return ClientDto.builder()
                .id(entity.getId())
                .clientType(entity.getClientType())
                .profileType(entity.getProfileType())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .companyName(entity.getCompanyName())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .cellphone(entity.getCellphone())
                .phone(entity.getPhone())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .birthDate(entity.getBirthDate())
                .legalRepresentativeIds(entity.getLegalRepresentativeIds() != null ? new ArrayList<>() : entity.getLegalRepresentativeIds())
                .authorizedSignatoryIds(entity.getAuthorizedSignatoryIds() != null ? new ArrayList<>() : entity.getAuthorizedSignatoryIds())
                .createAt(entity.getCreateAt())
                .status(entity.getStatus()).build();
    }

    public Client toEntity(ClientDto dto) {
        return Client.builder()
                .id(dto.getId())
                .clientType(dto.getClientType())
                .profileType(dto.getProfileType())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .companyName(dto.getCompanyName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .cellphone(dto.getCellphone())
                .phone(dto.getPhone())
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .birthDate(dto.getBirthDate())
                .legalRepresentativeIds(dto.getLegalRepresentatives() != null ? dto.getLegalRepresentatives().stream()
                        .map(LegalRepresentativeDto::getId).collect(Collectors.toList()) : new ArrayList<>())
                .authorizedSignatoryIds(dto.getAuthorizedSignatories() != null ? dto.getAuthorizedSignatories().stream()
                        .map(AuthorizedSignatoryDto::getId).collect(Collectors.toList()) : new ArrayList<>())
                .createAt(dto.getCreateAt())
                .status(dto.getStatus()).build();
    }
}
