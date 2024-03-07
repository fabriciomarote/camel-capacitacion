package com.camelcapacitacion.infraestructura.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestApiRoute extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .enableCORS(true);

        rest("/api/")
                .id("api-route")
                .produces("text/plain")
                .get("/saludo")
                .to("direct:getSaludo");

        from("direct:getSaludo")
                .setBody(constant("Hola Camel"));
    }
}