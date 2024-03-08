package com.camelcapacitacion.domain.application;

import com.camelcapacitacion.domain.model.Persona;

import java.util.List;

public interface PersonaService {
    public Persona buscarPorDni(String body);
    public List<Persona> obtenerTodasLasPersonas();
}
