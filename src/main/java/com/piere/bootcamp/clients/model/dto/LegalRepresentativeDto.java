package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.piere.bootcamp.clients.model.document.LegalRepresentative;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * LegalRepresentativeDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
 public class LegalRepresentativeDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private PersonDto person;

  private String personId;
  
  private Boolean status;

  public static LegalRepresentativeDto build() {
    return LegalRepresentativeDto.builder().build();
  }

  public LegalRepresentativeDto toDto(LegalRepresentative legalRepresentative) {
    return LegalRepresentativeDto.builder()
      .id(legalRepresentative.getId())
      .personId(legalRepresentative.getPersonId())
      .status(legalRepresentative.getStatus())
      .build();
  }

  public LegalRepresentative toEntity(LegalRepresentativeDto legalRepresentativeDto) {
    return LegalRepresentative.builder()
      .id(legalRepresentativeDto.getId())
      .personId(legalRepresentativeDto.getPersonId())
      .status(legalRepresentativeDto.getStatus())
      .build();
  }
}

