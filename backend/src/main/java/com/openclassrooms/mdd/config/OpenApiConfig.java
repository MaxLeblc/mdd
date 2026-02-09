package com.openclassrooms.mdd.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MDD API")
                        .version("1.0")
                        .description("Social network for developers - REST API documentation"))
                .tags(List.of(
                        new Tag().name("A. Authentication").description("Endpoints for user registration and login"),
                        new Tag().name("B. Topics").description("Endpoints for managing topics"),
                        new Tag().name("C. Users").description("Endpoints for user profile and subscriptions"),
                        new Tag().name("D. Posts").description("Endpoints for managing posts")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token (without 'Bearer ' prefix)")));
    }
}
