package com.camelcapacitacion.domain.repository;

import com.camelcapacitacion.domain.model.Persona;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PersonaRepository extends MongoRepository<Persona, String> {

    Persona findPersonaByDni(String dni);
    List<Persona> findAllByOrderByNombreAsc();

}