package com.piere.bootcamp.clients.model.document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.piere.bootcamp.clients.model.enums.DocumentTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;
import com.piere.bootcamp.clients.model.enums.ProfileTypeEnum;

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
@Document(collection = "clients")
public class Client implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(name = "client_type")
    private ClientTypeEnum clientType;

    @Field(name = "profile_type")
    private ProfileTypeEnum profileType;

    private String name;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "company_name")
    private String companyName;

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

    @Builder.Default
    @ToString.Exclude
    @Field(name = "legal_representative_ids")
    private List<String> legalRepresentativeIds = new ArrayList();
    
    @Builder.Default
    @ToString.Exclude
    @Field(name = "authorized_signatory_ids")
    private List<String> authorizedSignatoryIds = new ArrayList();

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Field(name = "create_at")
    private LocalDate createAt;

    private Boolean status;

}
