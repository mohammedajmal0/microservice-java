package com.project.microservices.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI productsApi(){
        return new OpenAPI().info(new Info().title("Product APIs")
                .description("Product APIs")
                .version("v0.1.0")
                .license(new License().name("Apache 2.0"))

        );
    }
}
