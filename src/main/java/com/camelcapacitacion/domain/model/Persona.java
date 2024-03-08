package com.camelcapacitacion.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document(collection = "personas")
public class Persona {
    @Id
    private String id;
    @JsonProperty("dni")
    private String dni;
    private String nombre;
}
