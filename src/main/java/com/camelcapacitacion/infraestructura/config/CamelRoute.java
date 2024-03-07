package com.camelcapacitacion.infraestructura.config;

import com.camelcapacitacion.infraestructura.domain.application.SucursalService;
import com.camelcapacitacion.infraestructura.domain.application.PersonaService;
import com.camelcapacitacion.infraestructura.domain.repository.PersonaRepository;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelRoute extends RouteBuilder {

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private SucursalService camelService;

    @Override
    public void configure() throws Exception {
        from("direct:saludo")
                .setBody().constant("Hola Camel!")
                .end();

        from("direct:getSucursales")
                .to("https://627303496b04786a09002b27.mockapi.io/mock/sucursales")
                .onException(Exception.class)
                    .log(LoggingLevel.ERROR,"Error al llamar a la API externa: ${exception.message}")
                    .setBody(simple("Error al llamar a la API externa"))
                    .setHeader("CamelHttpResponseCode", constant(500))
                    .end()
                .end()
                .log(LoggingLevel.INFO,"Respuesta de la API externa: ${body}")
                .choice()
                    .when(simple("${header.CamelHttpResponseCode} == 200"))
                        .convertBodyTo(String.class)
                    .otherwise()
                        .log(LoggingLevel.ERROR,"Error al llamar a la API externa. Código de respuesta: ${header.CamelHttpResponseCode}")
                        .setBody(simple("Error al llamar a la API externa. Código de respuesta: ${header.CamelHttpResponseCode}"))
                        .setHeader("CamelHttpResponseCode", constant(500))
                    .end()
                .end();

        from("direct:getIdsPares")
                .to("https://627303496b04786a09002b27.mockapi.io/mock/sucursales")
                .log(LoggingLevel.INFO,"Respuesta de la API externa: ${body}")
                .doTry()
                    .process(exchange -> {
                        String responseBody = exchange.getIn().getBody(String.class);
                        String idsPares = camelService.filtrarIdsPares(responseBody);
                        exchange.getIn().setBody(idsPares);
                    })
                .doCatch(Exception.class)
                    .log(LoggingLevel.ERROR,"Error al procesar IDs pares: ${exception.message}")
                    .setBody(simple("Error al procesar IDs pares"))
                    .setHeader("CamelHttpResponseCode", constant(500))
                .end();

        from("direct:buscarPorDni")
                .log(LoggingLevel.INFO,"Recibida solicitud para buscar persona por DNI: ${body}")
                .choice()
                    .when(simple("${body} == null || ${body['dni']} == null || ${body['dni']} == ''"))
                        .log(LoggingLevel.WARN,"DNI inválido o no proporcionado en la solicitud")
                        .setBody(simple("DNI inválido o no proporcionado"))
                        .setHeader("CamelHttpResponseCode", constant(400))
                    .otherwise()
                        .log(LoggingLevel.INFO,"Buscando persona por DNI: ${body['dni']}")
                        .bean(PersonaService.class, "buscarPorDni")
                        .choice()
                            .when(simple("${body} != null"))
                                .log(LoggingLevel.INFO,"Persona encontrada: ${body}")
                                .convertBodyTo(String.class)
                            .otherwise()
                                .log(LoggingLevel.WARN,"Persona no encontrada para el DNI: ${body['dni']}")
                                .setBody(simple("Persona no encontrada para el DNI: ${body['dni']}"))
                                .setHeader("CamelHttpResponseCode", constant(404))
                        .end()
                .end();

        from("direct:obtenerTodasLasPersonas")
                .log(LoggingLevel.INFO,"Obteniendo todas las personas")
                .bean(personaRepository, "findAllByOrderByNombreAsc")
                .log(LoggingLevel.INFO,"Personas encontradas: ${body}")
                .convertBodyTo(String.class);

        from("kafka:transaccion-topic?brokers=127.0.0.1:9092")
                .log(LoggingLevel.INFO,"Mensaje recibido: ${body}")
                .choice()
                    .when(simple("${body} == null"))
                        .log(LoggingLevel.ERROR, "Mensaje Kafka vacío o nulo")
                        .setBody(simple("Mensaje Kafka vacío o nulo"))
                        .setHeader("CamelHttpResponseCode", constant(400))
                    .otherwise()
                        .to("direct:procesarTransaccion")
                        .log(LoggingLevel.INFO,"Procesamiento de transaccion exitoso")
                        .setHeader("CamelHttpResponseCode", constant(200))
                .end();
    }
}