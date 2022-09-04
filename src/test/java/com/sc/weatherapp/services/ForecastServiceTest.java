package com.sc.weatherapp.services;


import com.sc.weatherapp.exception.ForecastNotFoundException;
import com.sc.weatherapp.model.Forecast;

import com.sc.weatherapp.model.ForecastHour;
import com.sc.weatherapp.repositories.ForecastRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


public class ForecastServiceTest {
    private static ForecastRepository forecastRepository;
    private static ForecastService forecastService;


    @BeforeAll
    public static void setup() {
        forecastRepository = Mockito.mock(ForecastRepository.class);
        forecastService = new ForecastService(forecastRepository);

    }
    @Test
    void createForecast_success() {
        ForecastHour forecastHour = ForecastHour.builder()
                .hour("18")
                .airTemperature("24")
                .pressure("769")
                .humidity("45")
                .build();
        Forecast sourceForecast = Forecast.builder()
                .id("1")
                .country("Ukraine")
                .city("Kharkov")
                .time(LocalDateTime.of(2022, Month.AUGUST, 12, 18, 0, 0, 0))
                .hourlyWeather(new ArrayList<>(Arrays.asList(forecastHour)))
                .build();


        Mockito.doReturn(Optional.of(sourceForecast)).when(forecastRepository).findById(Mockito.eq("1"));
        forecastService.createForecast(sourceForecast);

        Mockito.verify(forecastRepository).save(Mockito.eq(sourceForecast));
    }

    @Test
    void getForecast_success() {
        ForecastHour forecastHour = ForecastHour.builder()
                .hour("18")
                .airTemperature("24")
                .pressure("769")
                .humidity("45")
                .build();
        Forecast sourceForecast = Forecast.builder()
                .id("1")
                .country("Ukraine")
                .city("Kharkov")
                .time(LocalDateTime.of(2022, Month.AUGUST, 12, 18, 0, 0, 0))
                .hourlyWeather(new ArrayList<>(Arrays.asList(forecastHour)))
                .build();
        String id = "1";

        Mockito.doReturn(Optional.of(sourceForecast))
                .when(forecastRepository).findById(Mockito.eq("1"));

        Forecast actualForecast = forecastService.getForecast(id);

        Assertions.assertEquals(actualForecast,sourceForecast);

    }
    @Test
    void getForecast_failure() {
        String id = "1";

        Mockito.doReturn(Optional.empty())
                .when(forecastRepository).findById(Mockito.eq("1"));

        Assertions.assertThrows(ForecastNotFoundException.class,()->
                forecastService.getForecast(id));

    }
    @Test
    void deleteForecast_success() {
        String id = "1";

        Mockito.doReturn(true)
                .when(forecastRepository).existsById(Mockito.eq(id));

        forecastService.deleteForecast(id);

        Mockito.verify(forecastRepository).deleteById(Mockito.eq(id));
    }
    @Test
    void deleteForecast_failure() {
        String id = "1";

        Mockito.doReturn(false)
                .when(forecastRepository).existsById(Mockito.eq(id));

        String actualMessage = Assertions.assertThrows(ForecastNotFoundException.class,()->
                forecastService.deleteForecast(id)).getMessage();

        String expectedMessage = "Forecast with id: 1 not found!";

        Assertions.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    void updateForecast_success() {
        ForecastHour forecastHour = ForecastHour.builder()
                .hour("18")
                .airTemperature("24")
                .pressure("769")
                .humidity("45")
                .build();
        Forecast sourceForecast = Forecast.builder()
                .id("1")
                .country("Ukraine")
                .city("Kharkov")
                .time(LocalDateTime.of(2022, Month.AUGUST, 12, 18, 0, 0, 0))
                .hourlyWeather(new ArrayList<>(Arrays.asList(forecastHour)))
                .build();
        String id = "1";

        Mockito.doReturn(true)
                .when(forecastRepository).existsById(Mockito.eq("1"));
        Mockito.doReturn(Optional.of(sourceForecast))
                .when(forecastRepository).findById(Mockito.eq("1"));

        forecastService.updateForecast(id,sourceForecast);

        Forecast expectedForecast = Forecast.builder()
                .id("1")
                .country("Ukraine")
                .city("Kharkov")
                .time(LocalDateTime.of(2022, Month.AUGUST, 12, 18, 0, 0, 0))
                .hourlyWeather(new ArrayList<>(Arrays.asList(forecastHour)))
                .build();

        Mockito.verify(forecastRepository).save(Mockito.eq(expectedForecast));

    }
    @Test
    void updateForecast_failure() {
        ForecastHour forecastHour = ForecastHour.builder()
                .hour("18")
                .airTemperature("24")
                .pressure("769")
                .humidity("45")
                .build();
        Forecast sourceForecast = Forecast.builder()
                .id("1")
                .country("Ukraine")
                .city("Kharkov")
                .time(LocalDateTime.of(2022, Month.AUGUST, 12, 18, 0, 0, 0))
                .hourlyWeather(new ArrayList<>(Arrays.asList(forecastHour)))
                .build();
        String id = "1";

        Mockito.doReturn(false)
                .when(forecastRepository).existsById(Mockito.eq(id));

        String actualMessage = Assertions.assertThrows(ForecastNotFoundException.class, ()->
                forecastService.updateForecast(id,sourceForecast)).getMessage();

        String expectedMessage = "Forecast with id: 1 not found!";

        Assertions.assertEquals(actualMessage,expectedMessage);

    }

}
