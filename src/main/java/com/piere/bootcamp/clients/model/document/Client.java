package com.piere.bootcamp.clients.model.document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.piere.bootcamp.clients.model.enums.ClientTypeEnum;

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

    @Field(name = "person_id")
    private String personId;

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

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Field(name = "end_at")
    private LocalDate endAt;

    private Boolean status;
    
}
