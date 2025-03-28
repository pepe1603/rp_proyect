package com.buenrostroasociados.gestion_clientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Public API")
                .packagesToScan("com.buenrostroasociados.gestion_clientes.controller.publico")
                .build();
    }

    @Bean
    public GroupedOpenApi resourceApi() {
        return GroupedOpenApi.builder()
                .group("Resource API")
                .packagesToScan("com.buenrostroasociados.gestion_clientes.controller.resource")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Auth API")
                .packagesToScan("com.buenrostroasociados.gestion_clientes.controller.auth")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Clientes y Contabilidad")
                        .version("1.0.0")
                        .description("API para la gestión de clientes, actividades contables y litigios.")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Equipo de Soporte")
                                .url("https://example.com/support")
                                .email("support@buenrostroasociados.com"))
                        .license(new License()
                                .name("Licencia de Uso")
                                .url("https://buenrostroasociados.com/license")))
                .servers(List.of(
                        new Server().url("http://localhost:4200").description("Servidor Local de Desarrollo"),
                        new Server().url("https://staging.buenrostroasociados.com/v1").description("Servidor de Staging")
                ))
                .tags(List.of(
                        new Tag().name("Public").description("Endpoints accesibles públicamente."),
                        new Tag().name("Resource").description("Endpoints para operaciones CRUD y recursos protegidos."),
                        new Tag().name("Auth").description("Endpoints para autenticación y autorización.")
                ));
    }
}
