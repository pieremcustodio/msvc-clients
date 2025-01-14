package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.piere.bootcamp.clients.model.document.AuthorizedSignatory;

import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AuthorizedSignatoryDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizedSignatoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;


    private String name;

    private String lastName;

    private String email;

    private String address;

    private String cellphone;

    private String phone;

    private DocumentTypeEnum documentType;

    private String documentNumber;

    private Boolean status;

    public static AuthorizedSignatoryDto build() {
        return AuthorizedSignatoryDto.builder().build();
    }

    public AuthorizedSignatoryDto toDto(AuthorizedSignatory entity) {
        return AuthorizedSignatoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .cellphone(entity.getCellphone())
                .phone(entity.getPhone())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .status(entity.getStatus())
                .build();
    }

    public AuthorizedSignatory toEntity(AuthorizedSignatoryDto dto) {
        return AuthorizedSignatory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .cellphone(dto.getCellphone())
                .phone(dto.getPhone())
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .status(dto.getStatus())
                .build();
    }
}
