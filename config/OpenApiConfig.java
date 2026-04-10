package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI foodFiestaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ak_Food_corner API")
                        .description("Comprehensive API documentation for Ak_Food_corner - Premium Dining Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Ak_Food_corner Support")
                                .email("support@foodfiesta.com")));
    }
}
