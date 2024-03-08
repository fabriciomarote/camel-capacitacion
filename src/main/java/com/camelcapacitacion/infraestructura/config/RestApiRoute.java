package com.camelcapacitacion.infraestructura.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestApiRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("platform-http:/api/camel/saludo?httpMethodRestrict=GET&produces=text/plain")
            .log("Recibida una solicitud en /api/saludo")
            .setBody(constant("Hola Camel"));
    }
}