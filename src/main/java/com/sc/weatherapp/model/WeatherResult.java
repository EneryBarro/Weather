package com.sc.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherResult {

    @JsonProperty("City")
    String city;
    @JsonProperty("Country")
    String country;
    @JsonProperty("Forecast")
    List<Forecast> forecasts;

}
