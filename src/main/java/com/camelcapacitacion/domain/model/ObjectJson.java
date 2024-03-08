package com.camelcapacitacion.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ObjectJson {
    @JsonProperty("createdAt")
    private String createdAt;
    private String id;
    private String nombre;
    private String direccion;
    private String genero;
    private String pais;
    private String perro;
    private List<String> geo;

}
