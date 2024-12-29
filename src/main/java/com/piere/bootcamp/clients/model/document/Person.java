package com.piere.bootcamp.clients.model.document;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "persons")
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(name = "trade_name")
    private String tradeName;

    @Field(name = "company_name")
    private String companyName;

    private String name;

    @Field(name = "last_name")
    private String lastName;

    private String email;

    private String address;

    private String cellphone;

    private String phone;

    @Field(name = "document_type")
    private DocumentTypeEnum documentType;

    @Field(name = "document_number")
    @Indexed(unique = true)
    private String documentNumber;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Field(name = "birth_date")
    private LocalDate birthDate;
}
