package com.microservices.sales.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient inventoryWebClient(
            WebClient.Builder builder,
            @Value("${clients.inventory.base-url:http://localhost:8083}") String inventoryBaseUrl) {
        return builder.baseUrl(inventoryBaseUrl).build();
    }
}
