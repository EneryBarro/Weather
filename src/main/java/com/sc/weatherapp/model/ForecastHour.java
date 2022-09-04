package com.sc.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForecastHour {

    @JsonProperty("Hour")
    String hour;
    @JsonProperty("Air temperature")
    String airTemperature;
    @JsonProperty("Pressure")
    String pressure;
    @JsonProperty("Humidity")
    String humidity;

}
