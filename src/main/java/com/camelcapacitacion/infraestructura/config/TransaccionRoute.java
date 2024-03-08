package com.camelcapacitacion.infraestructura.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransaccionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:procesarTransaccion")
                .log("Procesando transacción: ${body}")
                .to("log:output")
                .end();
    }
}