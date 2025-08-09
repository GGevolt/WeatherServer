package com.ggevolt.weatherserver.services;

import com.ggevolt.weatherserver.configs.ApiKey;
import com.ggevolt.weatherserver.models.WeatherVM;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class OpenWeatherServiceTest {
    private static MockWebServer mockWebServer;
    private OpenWeatherService service;


    @BeforeAll
    static void setUpServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        ApiKey apiKey = new ApiKey();
        apiKey.setOpenWeather("test-api-key");
        WebClient.Builder builder = WebClient.builder();
        String baseUrl = mockWebServer.url("/").toString();

        service = new OpenWeatherService(builder, apiKey, baseUrl);
    }

    @Test
    void getWeather_success() {
        String body = "{   \"cod\": \"200\",\n" +
                "    \"message\": 0,\n" +
                "    \"cnt\": 3,\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"dt\": 1754686800,\n" +
                "            \"main\": {\n" +
                "                \"temp\": 26.65,\n" +
                "                \"feels_like\": 26.65,\n" +
                "                \"temp_min\": 26.34,\n" +
                "                \"temp_max\": 26.65,\n" +
                "                \"pressure\": 1008,\n" +
                "                \"sea_level\": 1008,\n" +
                "                \"grnd_level\": 1003,\n" +
                "                \"humidity\": 78,\n" +
                "                \"temp_kf\": 0.31\n" +
                "            },\n" +
                "            \"weather\": [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10n\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"clouds\": {\n" +
                "                \"all\": 77\n" +
                "            },\n" +
                "            \"wind\": {\n" +
                "                \"speed\": 2.25,\n" +
                "                \"deg\": 203,\n" +
                "                \"gust\": 2.73\n" +
                "            },\n" +
                "            \"visibility\": 10000,\n" +
                "            \"pop\": 0.2,\n" +
                "            \"rain\": {\n" +
                "                \"3h\": 0.25\n" +
                "            },\n" +
                "            \"sys\": {\n" +
                "                \"pod\": \"n\"\n" +
                "            },\n" +
                "            \"dt_txt\": \"2025-08-08 21:00:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1754697600,\n" +
                "            \"main\": {\n" +
                "                \"temp\": 26.82,\n" +
                "                \"feels_like\": 29.02,\n" +
                "                \"temp_min\": 26.82,\n" +
                "                \"temp_max\": 26.82,\n" +
                "                \"pressure\": 1009,\n" +
                "                \"sea_level\": 1009,\n" +
                "                \"grnd_level\": 1004,\n" +
                "                \"humidity\": 76,\n" +
                "                \"temp_kf\": 0\n" +
                "            },\n" +
                "            \"weather\": [\n" +
                "                {\n" +
                "                    \"id\": 500,\n" +
                "                    \"main\": \"Rain\",\n" +
                "                    \"description\": \"light rain\",\n" +
                "                    \"icon\": \"10d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"clouds\": {\n" +
                "                \"all\": 87\n" +
                "            },\n" +
                "            \"wind\": {\n" +
                "                \"speed\": 3.16,\n" +
                "                \"deg\": 193,\n" +
                "                \"gust\": 3.86\n" +
                "            },\n" +
                "            \"visibility\": 10000,\n" +
                "            \"pop\": 0.2,\n" +
                "            \"rain\": {\n" +
                "                \"3h\": 0.26\n" +
                "            },\n" +
                "            \"sys\": {\n" +
                "                \"pod\": \"d\"\n" +
                "            },\n" +
                "            \"dt_txt\": \"2025-08-09 00:00:00\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"dt\": 1754708400,\n" +
                "            \"main\": {\n" +
                "                \"temp\": 29.66,\n" +
                "                \"feels_like\": 33.16,\n" +
                "                \"temp_min\": 29.66,\n" +
                "                \"temp_max\": 29.66,\n" +
                "                \"pressure\": 1008,\n" +
                "                \"sea_level\": 1008,\n" +
                "                \"grnd_level\": 1004,\n" +
                "                \"humidity\": 65,\n" +
                "                \"temp_kf\": 0\n" +
                "            },\n" +
                "            \"weather\": [\n" +
                "                {\n" +
                "                    \"id\": 804,\n" +
                "                    \"main\": \"Clouds\",\n" +
                "                    \"description\": \"overcast clouds\",\n" +
                "                    \"icon\": \"04d\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"clouds\": {\n" +
                "                \"all\": 100\n" +
                "            },\n" +
                "            \"wind\": {\n" +
                "                \"speed\": 2.35,\n" +
                "                \"deg\": 183,\n" +
                "                \"gust\": 2.99\n" +
                "            },\n" +
                "            \"visibility\": 10000,\n" +
                "            \"pop\": 0,\n" +
                "            \"sys\": {\n" +
                "                \"pod\": \"d\"\n" +
                "            },\n" +
                "            \"dt_txt\": \"2025-08-09 03:00:00\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"city\": {\n" +
                "        \"id\": 1583992,\n" +
                "        \"name\": \"Turan\",\n" +
                "        \"coord\": {\n" +
                "            \"lat\": 0.0,\n" +
                "            \"lon\": 0.0 \n" +
                "        },\n" +
                "        \"country\": \"VN\",\n" +
                "        \"population\": 752493,\n" +
                "        \"timezone\": 25200,\n" +
                "        \"sunrise\": 1754692211,\n" +
                "        \"sunset\": 1754738101\n" +
                "    }\n" +
                "}";
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(body)
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
        );

        Mono<WeatherVM> result = service.getWeather(0.0, 0.0);

        StepVerifier.create(result)
                .consumeNextWith(System.out::println)
                .verifyComplete();
    }

    @Test
    void getWeather_missingApiKey() {
        ApiKey missingKey = new ApiKey();
        missingKey.setOpenWeather(null);
        WebClient.Builder builder = WebClient.builder();
        String baseUrl = mockWebServer.url("/").toString();
        OpenWeatherService svcWithMissingKey = new OpenWeatherService(builder, missingKey, baseUrl);

        Mono<WeatherVM> result = svcWithMissingKey.getWeather(10.0, 20.0);
        StepVerifier.create(result)
                .expectErrorMatches(e ->
                        e instanceof IllegalStateException &&
                                e.getMessage().equals("OpenWeather API key is not configured"))
                .verify();
    }


    @Test
    void getWeather_noArgument() {
        Mono<WeatherVM> result = service.getWeather(Double.NaN, Double.NaN);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Latitude and longitude must be valid numbers"))
                .verify();

        Mono<WeatherVM> result2 = service.getWeather(-92.0, 98.0);
        StepVerifier.create(result2)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Latitude must be between -90 and 90"))
                .verify();

        Mono<WeatherVM> result3 = service.getWeather(-80.2, 193.1);
        StepVerifier.create(result3)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Longitude must be between -180 and 180"))
                .verify();
    }

    @Test
    void getWeather_error() {
        mockWebServer.enqueue(new MockResponse().setBody("error").setResponseCode(511));

        Mono<WeatherVM> result = service.getWeather(0.0, 0.0);

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }
}