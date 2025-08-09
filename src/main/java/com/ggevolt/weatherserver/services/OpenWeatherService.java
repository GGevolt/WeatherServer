package com.ggevolt.weatherserver.services;

import com.ggevolt.weatherserver.configs.ApiKey;
import com.ggevolt.weatherserver.models.WeatherVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class OpenWeatherService {

    private WebClient client;

    private ApiKey apiKey;

    private String baseUrl = "https://api.openweathermap.org";

    // Default constructor: allows manual instantiation (e.g. new OpenWeatherService())
    public OpenWeatherService() {
        this.client = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = new ApiKey(); // empty; will cause a clear error if getWeather is called without configuration
    }

    // Optional Spring-style constructor injection
    @Autowired
    public OpenWeatherService(
            WebClient.Builder webClientBuilder,
            ApiKey apiKey,
            @Value("${openweather.base-url:https://api.openweathermap.org}") String baseUrl
    ) {
        this.baseUrl = baseUrl;
        this.client = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    // Setter injection: works if Spring wants to wire after default construction
    @Autowired
    public void setApiKey(ApiKey apiKey) {
        this.apiKey = apiKey;
    }

    @Autowired
    public void setWebClientBuilder(
            WebClient.Builder webClientBuilder,
            @Value("${openweather.base-url:https://api.openweathermap.org}") String baseUrl
    ) {
        this.baseUrl = baseUrl;
        this.client = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<WeatherVM> getWeather(Double latitude, Double longitude) {
        var openWeatherApiKey = apiKey.getOpenWeather();
        if (openWeatherApiKey == null || openWeatherApiKey.isBlank()) {
            return Mono.error(new IllegalStateException("OpenWeather API key is not configured"));
        }

        if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
            return Mono.error(new IllegalArgumentException("Latitude and longitude must be valid numbers"));
        }
        if (latitude < -90 || latitude > 90) {
            return Mono.error(new IllegalArgumentException("Latitude must be between -90 and 90"));
        }
        if (longitude < -180 || longitude > 180) {
            return Mono.error(new IllegalArgumentException("Longitude must be between -180 and 180"));
        }
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/forecast")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("units", "metric")
                        .queryParam("cnt", 3)
                        .queryParam("lang", "en")
                        .queryParam("appid", openWeatherApiKey)
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new WebClientResponseException(
                                        "API error: " + errorBody,
                                        clientResponse.statusCode().value(),
                                        null,
                                        null, null, null
                                )))
                )
                .bodyToMono(WeatherVM.class);
    }
}

