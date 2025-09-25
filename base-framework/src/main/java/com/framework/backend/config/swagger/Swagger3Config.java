package com.framework.backend.config.swagger;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
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
    info = @Info(title = "Swagger3", version = "1.0", description = "后台框架使用手册"),
    security = @SecurityRequirement(name = "x-auth-token"),
    externalDocs =
        @ExternalDocumentation(
            description = "参考文档",
            url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"))
@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    name = "x-auth-token",
    in = SecuritySchemeIn.HEADER)
public class Swagger3Config {}
