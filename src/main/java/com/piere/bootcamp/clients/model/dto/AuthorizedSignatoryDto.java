package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.piere.bootcamp.clients.model.document.AuthorizedSignatory;

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

    private PersonDto person;

    private String personId;
    
    private Boolean status;

    public static AuthorizedSignatoryDto build() {
      return AuthorizedSignatoryDto.builder().build();
    }

    public AuthorizedSignatoryDto toDto(AuthorizedSignatory authorizedSignatory) {
      return AuthorizedSignatoryDto.builder()
        .id(authorizedSignatory.getId())
        .personId(authorizedSignatory.getPersonId())
        .status(authorizedSignatory.getStatus())
        .build();
    }

    public AuthorizedSignatory toEntity(AuthorizedSignatoryDto authorizedSignatoryDto) {
      return AuthorizedSignatory.builder()
        .id(authorizedSignatoryDto.getId())
        .personId(authorizedSignatoryDto.getPersonId())
        .status(authorizedSignatoryDto.getStatus())
        .build();
    }
}
