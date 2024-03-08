package com.camelcapacitacion.infraestructura.api;

import com.camelcapacitacion.domain.model.Persona;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceController {
    private final ProducerTemplate producerTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ServiceController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    /**
     * Endpoint para obtener un saludo.
     *
     * @return El saludo obtenido de la ruta Camel.
     */
    @GetMapping("/saludo")
    public String getSaludo() {
        return producerTemplate.requestBody("direct:saludo", null, String.class);
    }

    /**
     * Endpoint para obtener información de sucursales.
     *
     * @return La información de sucursales obtenida de la ruta Camel.
     */
    @GetMapping("/sucursales")
    public String getSucursales() {
        return producerTemplate.requestBody("direct:getSucursales", null, String.class);
    }

    /**
     * Endpoint para obtener los IDs pares de la ruta Camel.
     *
     * @return Los IDs pares obtenidos de la ruta Camel o un mensaje de error en caso de fallo.
     */
    @GetMapping("/pares")
    public ResponseEntity<String> getIdsPares() {
        try {
            String result = producerTemplate.requestBody("direct:getIdsPares", null, String.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    /**
     * Endpoint para buscar una persona por su DNI.
     *
     * @param dni El DNI de la persona a buscar.
     * @return La información de la persona si se encuentra, un código de error en caso contrario.
     */
    @GetMapping("/personas/{dni}")
    public ResponseEntity<Persona> buscarPorDni(@PathVariable String dni) {
        try {
            if (!validarFormatoDni(dni)) {
                return ResponseEntity.badRequest().build();
            }

            Persona body = new Persona();
            body.setDni(dni);

            String jsonBody = objectMapper.writeValueAsString(body);
            Persona persona = producerTemplate.requestBody("direct:buscarPorDni", jsonBody, Persona.class);

            if (persona != null) {
                return ResponseEntity.ok(persona);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para obtener todas las personas.
     *
     * @return Lista de todas las personas obtenida de la ruta Camel.
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/personas")
    public List<Persona> obtenerTodasLasPersonas() {
        return producerTemplate.requestBody("direct:obtenerTodasLasPersonas", null, List.class);
    }

    /**
     * Método para validar el formato del DNI.
     *
     * @param dni El DNI a validar.
     * @return True si el formato es válido, False en caso contrario.
     */
    private boolean validarFormatoDni(String dni) {
        return dni.matches("^[0-9]{8}$");
    }
}