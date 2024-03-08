package com.camelcapacitacion.domain.application.impl;

import com.camelcapacitacion.domain.application.PersonaService;
import com.camelcapacitacion.domain.model.Persona;
import com.camelcapacitacion.domain.repository.PersonaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Busca una persona por su DNI en la base de datos.
     *
     * @param body El cuerpo de la solicitud que contiene la información de la persona como formato JSON.
     * @return La información de la persona si se encuentra, o null si no se encuentra en la base de datos.
     * @throws RuntimeException Si hay un error durante el procesamiento de la solicitud.
     */
    @Override
    public Persona buscarPorDni(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Persona newBody = objectMapper.readValue(body, Persona.class);

            return personaRepository.findPersonaByDni(newBody.getDni());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar la solicitud JSON", e);
        }
    }

    /**
     * Obtiene la lista de todas las personas almacenadas en la base de datos, ordenadas por nombre de forma ascendente.
     *
     * @return Lista de personas ordenadas por nombre.
     */
    @Override
    public List<Persona> obtenerTodasLasPersonas() {
        return personaRepository.findAllByOrderByNombreAsc();
    }
}
