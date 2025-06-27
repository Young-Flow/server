package com.pitchain.common.config;

import com.pitchain.common.apiPayload.ErrorResponseDTO;
import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.annotation.ErrorApiResponse;
import com.pitchain.common.annotation.ErrorApiResponses;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.Elements.JWT;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.server}")
    private String serverUrl;
    private final MessageSource messageSource;

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
                .servers(List.of(new Server().url(serverUrl)))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Pitchain API")
                .version("v0.0.1");
    }

    @Bean
    public OperationCustomizer customizeResponses() {
        return (operation, handlerMethod) -> {
            processErrorApiResponse(operation, handlerMethod.getMethodAnnotation(ErrorApiResponse.class));
            processErrorApiResponses(operation, handlerMethod.getMethodAnnotation(ErrorApiResponses.class));
            return operation;
        };
    }

    private void processErrorApiResponse(Operation operation, ErrorApiResponse errorApiResponse) {
        if (errorApiResponse != null) {
            addErrorResponse(operation.getResponses(), errorApiResponse.value());
        }
    }

    private void processErrorApiResponses(Operation operation, ErrorApiResponses errorApiResponses) {
        if (errorApiResponses != null) {
            ApiResponses responses = operation.getResponses();
            for (ErrorStatus errorStatus : errorApiResponses.value()) {
                addErrorResponse(responses, errorStatus);
            }
        }
    }

    private void addErrorResponse(ApiResponses responses, ErrorStatus errorStatus) {
        ErrorResponseDTO error = errorStatus.getCustomResponseDTO(messageSource);
        String code = error.getCode();
        String description = error.getMessage();
        Content errorContent = new Content().addMediaType("application/json", new MediaType().schema(
                new Schema<>()
                        .type("object")
                        .addProperty("code", new StringSchema()._default(code))
                        .addProperty("message", new StringSchema()._default(description))
        ));
        responses.put(code, new ApiResponse()
                .description(description)
                .content(errorContent));
    }
}
