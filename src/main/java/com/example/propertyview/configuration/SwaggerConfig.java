package com.example.propertyview.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.propertyview.util.ConstantUtil.API_DESCRIPTION;
import static com.example.propertyview.util.ConstantUtil.API_GROUP;
import static com.example.propertyview.util.ConstantUtil.API_PACKAGE_TO_SCAN;
import static com.example.propertyview.util.ConstantUtil.API_TITLE;
import static com.example.propertyview.util.ConstantUtil.API_VERSION;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(API_GROUP)
                .packagesToScan(API_PACKAGE_TO_SCAN)
                .build();
    }

    @Bean
    public OpenAPI propertyViewOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION))
                .externalDocs(new ExternalDocumentation());
    }
}
