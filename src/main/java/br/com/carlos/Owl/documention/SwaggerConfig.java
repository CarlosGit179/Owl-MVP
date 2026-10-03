package br.com.carlos.Owl.documention;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/** Provides the API metadata displayed by the generated OpenAPI documentation. */
@Configuration
public class SwaggerConfig {

    /**
     * Defines the API title, version, and purpose for the OpenAPI document.
     *
     * @return the OpenAPI metadata configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Owl API").version("1.0")
                .description("REST API for managing the library, books, students, and loans."));
    }

}
