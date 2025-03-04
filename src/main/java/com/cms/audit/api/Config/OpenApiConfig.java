package com.cms.audit.api.Config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .displayName("Audit Services")
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }
}

