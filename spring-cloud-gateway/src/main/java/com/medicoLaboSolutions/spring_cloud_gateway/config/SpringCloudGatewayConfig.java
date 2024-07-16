package com.medicoLaboSolutions.spring_cloud_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("microservice-patient", r -> r.path("/patients/**")
                        .uri("http://localhost:9001/"))
                .route("microservice-note", r -> r.path("/notes/**")
                        .uri("http://localhost:9002/"))
                .route("microservice-diagnostics", r -> r.path("/diagnostics/**")
                        .uri("http://localhost:9003/"))
                .build();
    }
}
