package com.sc.weatherapp.utils.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.weatherapp.exception.WeatherException;
import com.sc.weatherapp.model.Forecast;
import com.sc.weatherapp.model.ForecastHour;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class ForecastMapper {

    public static List<Forecast> mapToForecast(String weatherData) {
        try {
            List<ForecastHour> hours = new ArrayList<>();
            List<Forecast> result = new ArrayList<>();
            new ObjectMapper().readTree(weatherData).findValue("hours").forEach((node) -> {
                hours.add(ForecastHour.builder()
                        .airTemperature(
                                String.format("%.1f", node.findValue("airTemperature").findValue("sg").asDouble(0)))
                        .humidity(String.format("%.2f", node.findValue("humidity").findValue("sg").asDouble(0)))
                        .pressure(String.format("%.1f", node.findValue("pressure").findValue("sg").asDouble(0) / 1000))
                        .hour(LocalDateTime.parse(node.findValue("time").asText("0000-00-00T00:00:00+00:00"),
                                DateTimeFormatter.ISO_DATE_TIME).getHour() + ":00")
                        .build());
                
                if (hours.size() == 24) {
                    result.add(Forecast.builder()
                            .time(LocalDateTime.parse(node.findValue("time").asText("0000-00-00T00:00:00+00:00"),
                                    DateTimeFormatter.ISO_DATE_TIME))
                            .hourlyWeather(new ArrayList<>(hours))
                            .build());
                    hours.clear();
                }
            });
            return result;
        } catch (JsonProcessingException exception) {
            throw new WeatherException("Invalid weather data!");
        }
    }
}