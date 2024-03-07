package com.camelcapacitacion.infraestructura.domain.repository;

import com.camelcapacitacion.infraestructura.domain.model.Persona;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PersonaRepository extends MongoRepository<Persona, String> {

    Persona findPersonaByDni(String dni);

    List<Persona> findAllByOrderByNombreAsc();

    Persona findPersonaById(String id);
}