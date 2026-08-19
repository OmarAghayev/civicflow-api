package com.omaragayev.civicflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI civicFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CivicFlow: Public Services & Permit Processing Engine")
                        .description("High-throughput Enterprise API for digital public permits, citizen requests, and automated verification workflows.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Omar Aghayev")
                                .email("contact@omaragayev.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}