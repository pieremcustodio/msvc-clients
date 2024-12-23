package com.piere.bootcamp.clients.model.dto;

import java.time.LocalDate;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.piere.bootcamp.clients.model.document.Person;
import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * PersonDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String tradeName;

  private String companyName;

  private String name;

  private String lastname;

  private String email;

  private String address;

  private String cellphone;

  private String phone;

  private DocumentTypeEnum documentType;

  private String documentNumber;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate birthDate;

  public static PersonDto build() {
    return PersonDto.builder().build();
  }

  public PersonDto toDto(Person person) {
    return PersonDto.builder()
        .id(person.getId())
        .tradeName(person.getTradeName())
        .companyName(person.getCompanyName())
        .name(person.getName())
        .lastname(person.getLastName())
        .email(person.getEmail())
        .address(person.getAddress())
        .cellphone(person.getCellphone())
        .phone(person.getPhone())
        .documentType(person.getDocumentType())
        .documentNumber(person.getDocumentNumber())
        .birthDate(person.getBirthDate())
        .build();
  }


  public Person toEntity(PersonDto personDto) {
    return Person.builder()
        .id(personDto.getId())
        .tradeName(personDto.getTradeName())
        .companyName(personDto.getCompanyName())
        .name(personDto.getName())
        .lastName(personDto.getLastname())
        .email(personDto.getEmail())
        .address(personDto.getAddress())
        .cellphone(personDto.getCellphone())
        .phone(personDto.getPhone())
        .documentType(personDto.getDocumentType())
        .documentNumber(personDto.getDocumentNumber())
        .birthDate(personDto.getBirthDate())
        .build();
  }
}

