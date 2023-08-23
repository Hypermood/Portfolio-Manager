package com.melon.portfoliomanager.configs;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI PortfolioManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Portfolio Manager API")
                        .description("Application for tracking your stocks and getting valuable insights about their value.")
                        .version("1.0"));
    }
}
