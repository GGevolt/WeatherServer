package com.ggevolt.weatherserver.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public ConnectionProvider webClientConnectionProvider() {
        return ConnectionProvider.builder("webclient-pool")
                .maxConnections(200)
                .pendingAcquireMaxCount(1_000)
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .lifo()
                .build();
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ConnectionProvider provider) {
        HttpClient httpClient = HttpClient.create(provider)
                .compress(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
                .responseTimeout(Duration.ofSeconds(10))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));
        return new ReactorClientHttpConnector(httpClient);
    }

    // Preferred for creating per-API clients with custom baseUrl/headers
    @Bean
    public WebClient.Builder webClientBuilder(ReactorClientHttpConnector connector) {
        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofRequestProcessor(Mono::just))
                .filter(ExchangeFilterFunction.ofResponseProcessor(Mono::just));
    }


    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

}
