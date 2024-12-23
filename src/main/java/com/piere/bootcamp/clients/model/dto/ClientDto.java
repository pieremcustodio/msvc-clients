package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
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

  private PersonDto person;

  private String personId;

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

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate endAt;

  private Boolean status;

  public static ClientDto build() {
    return ClientDto.builder().build();
  }

  public ClientDto toDto(Client client) {
    return ClientDto.builder()
      .id(client.getId())
      .clientType(client.getClientType())
      .personId(client.getPersonId())
      .legalRepresentativeIds(client.getLegalRepresentativeIds())
      .authorizedSignatoryIds(client.getAuthorizedSignatoryIds())
      .createAt(client.getCreateAt())
      .endAt(client.getEndAt())
      .status(client.getStatus())
      .build();
  }

  public Client toEntity(ClientDto clientDto) {
    return Client.builder()
      .id(clientDto.getId())
      .clientType(clientDto.getClientType())
      .personId(clientDto.getPerson().getId())
      .legalRepresentativeIds(clientDto.getLegalRepresentatives().stream().map(LegalRepresentativeDto::getId).collect(Collectors.toList()))
      .authorizedSignatoryIds(clientDto.getAuthorizedSignatories().stream().map(AuthorizedSignatoryDto::getId).collect(Collectors.toList()))
      .createAt(clientDto.getCreateAt())
      .endAt(clientDto.getEndAt())
      .status(clientDto.getStatus())
      .build();
  }
}