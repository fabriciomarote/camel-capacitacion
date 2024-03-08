package com.camelcapacitacion.domain.application.impl;

import com.camelcapacitacion.domain.application.SucursalService;
import com.camelcapacitacion.domain.model.ObjectJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SucursalServiceImpl implements SucursalService {

    /**
     * Filtra los IDs pares de una lista de objetos JSON.
     *
     * @param responseBody La respuesta JSON de la que se extraen los IDs.
     * @return Una cadena que contiene los IDs pares separados por comas.
     * @throws RuntimeException Si hay un error al procesar la respuesta JSON.
     */
    @Override
    public String filtrarIdsPares(String responseBody) {
    // Convierte la respuesta JSON en una lista de objetos
    List<ObjectJson> listaDeObjetos = convertirJsonALista(responseBody);

    List<String> idsPares = listaDeObjetos.stream()
            .map(ObjectJson::getId)
            .filter(id -> Integer.parseInt(id) % 2 == 0)
            .collect(Collectors.toList());

    return String.join(",", idsPares);
}

    /**
     * Convierte una cadena JSON en una lista de objetos.
     *
     * @param responseBody La respuesta JSON que se va a convertir.
     * @return Una lista de objetos extraídos de la respuesta JSON.
     * @throws RuntimeException Si hay un error al convertir la respuesta JSON a una lista de objetos.
     */
    private List<ObjectJson> convertirJsonALista(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, new TypeReference<List<ObjectJson>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a lista", e);
        }
    }
}