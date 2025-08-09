package com.ggevolt.weatherserver.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherEntry {
    @JsonProperty("main")
    private AdditionalInfo additionalInfo;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private int visibility;
    private Sys sys;
    @JsonProperty("dt_txt")
    private String timestamp;
}
