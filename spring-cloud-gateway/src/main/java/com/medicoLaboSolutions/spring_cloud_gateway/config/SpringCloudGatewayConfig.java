package com.medicoLaboSolutions.spring_cloud_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SpringCloudGatewayConfig {
    @Autowired
    private Environment environment;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("microservice-patient", r -> r.path("/patients/**")
                        .uri("http://" + environment.getProperty("ms.patient.url") + ":"+ environment.getProperty("ms.patient.port") +"/"))
                .route("microservice-note", r -> r.path("/notes/**")
                        .uri("http://" + environment.getProperty("ms.note.url") + ":"+ environment.getProperty("ms.note.port") +"/"))
                .route("microservice-diagnostics", r -> r.path("/diagnostics/**")
                        .uri("http://" + environment.getProperty("ms.diagnostic.url") + ":"+ environment.getProperty("ms.diagnostic.port") +"/"))
                .build();
    }
}
