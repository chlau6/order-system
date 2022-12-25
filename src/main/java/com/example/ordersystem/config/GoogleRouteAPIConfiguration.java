package com.example.ordersystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class GoogleRouteAPIConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public RestTemplate restTemplate() {
        System.out.println(env.getProperty("spring.datasource.url"));
        return new RestTemplateBuilder()
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-Goog-FieldMask", "routes.distanceMeters")
                .defaultHeader("X-Goog-Api-Key", env.getProperty("google.map.api.key"))
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}
