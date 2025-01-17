package com.pitchain.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.Elements.JWT;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pitchainOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                AUTHORIZATION,
                                new SecurityScheme()
                                        .name(AUTHORIZATION)
                                        .type(HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat(JWT))
                )
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .externalDocs(new ExternalDocumentation()
                        .description("Pitchain Server Github")
                        .url("https://github.com/Young-Flow/server"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Pitchain API")
                .version("v0.0.1");
    }

}
