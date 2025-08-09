package com.ggevolt.weatherserver.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "apikey")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyConfig {
    private String openWeather;
}
