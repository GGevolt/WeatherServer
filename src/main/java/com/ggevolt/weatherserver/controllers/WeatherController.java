package com.ggevolt.weatherserver.controllers;

import com.ggevolt.weatherserver.services.OpenWeatherService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/weather", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherController {

    private final OpenWeatherService service;

    public WeatherController(OpenWeatherService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> getWeather(@RequestParam Double latitude, @RequestParam Double longitude) {
        return service.getWeather(latitude, longitude)
                .map(body -> ResponseEntity.ok().body((Object) body))
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400))))
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), 400))))
                .onErrorResume(WebClientResponseException.class, e ->
                        Mono.just(ResponseEntity.status(e.getStatusCode().value())
                                .body(new ErrorResponse("Remote API error: " + e.getStatusCode().value(), e.getStatusCode().value()))))
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.internalServerError()
                                .body(new ErrorResponse("Unexpected error", 500))));
    }

    // Simple JSON error payload
    public record ErrorResponse(String error, int status) {}


}

