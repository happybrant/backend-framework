package com.framework.backend.config.swagger;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * @author fucong
 * @since 2025/7/22 17:02
 * @description Swagger文档配置
 */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Swagger3",
            version = "1.0",
            description = "后台框架使用手册",
            contact = @Contact(name = "TOM")),
    security = @SecurityRequirement(name = "JWT"),
    externalDocs =
        @ExternalDocumentation(
            description = "参考文档",
            url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"))
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER)
public class Swagger3Config {}
