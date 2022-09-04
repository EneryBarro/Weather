package com.sc.weatherapp.services;

import com.sc.weatherapp.api.LocationApi;
import com.sc.weatherapp.api.WeatherApi;
import com.sc.weatherapp.model.Forecast;
import com.sc.weatherapp.model.ForecastHour;
import com.sc.weatherapp.model.Location;
import com.sc.weatherapp.model.WeatherResult;
import com.sc.weatherapp.repositories.ForecastRepository;
import com.sc.weatherapp.utils.uuid.ForecastIdHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherServiceTest {

    public static final String IP = "192.168.1.1";
    public static final String CITY = "Kyiv";
    public static final String COUNTRY = "Ukraine";

    private static WeatherApi weatherApi;
    private static LocationApi locationApi;
    private static ForecastRepository forecastRepository;
    private static String apiResponse;
    private static Location location;
    private static List<Forecast> remoteForecasts;
    private static List<Forecast> forecasts;

    @BeforeAll
    static void setup() throws IOException {
        weatherApi = Mockito.mock(WeatherApi.class);
        locationApi = Mockito.mock(LocationApi.class);
        forecastRepository = Mockito.mock(ForecastRepository.class);
        forecasts = new ArrayList<>();
        remoteForecasts = new ArrayList<>();
        location = Location.builder()
                .city(CITY)
                .country(COUNTRY)
                .latitude(1)
                .longitude(1)
                .build();
        setupMockForecasts();
        apiResponse = new String(
                Files.readAllBytes(new ClassPathResource("ApiResponse.json")
                        .getFile()
                        .toPath())
        );
        remoteForecasts.forEach(ForecastIdHelper::addId);
    }

    @AfterEach
    void resetMocks() {
        weatherApi = Mockito.mock(WeatherApi.class);
        locationApi = Mockito.mock(LocationApi.class);
        forecastRepository = Mockito.mock(ForecastRepository.class);
        forecasts.clear();
    }

    @Test
    void getMainPageWeatherTest_FromDB_success() {
        for (int i = 0; i < 10; i++) {
            forecasts.add(new Forecast());
        }
        WeatherResult expected = WeatherResult.builder()
                .country(COUNTRY)
                .city(CITY)
                .forecasts(forecasts)
                .build();

        Mockito.when(locationApi.getLocation(Mockito.eq(IP))).thenReturn(location);
        Mockito.when(forecastRepository.findByCountryAndCity(
                    Mockito.any(),
                    Mockito.any(),
                    Mockito.eq(COUNTRY),
                    Mockito.eq(CITY)
                )).thenReturn(forecasts);

        WeatherResult actual = new WeatherService(weatherApi, locationApi, forecastRepository).getMainPageWeather(IP);

        Mockito.verifyNoInteractions(weatherApi);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getMainPageWeatherTest_FromDB_failure() {
        for (int i = 0; i < 9; i++) {
            forecasts.add(new Forecast());
        }
        WeatherResult expected = WeatherResult.builder()
                .country(COUNTRY)
                .city(CITY)
                .forecasts(remoteForecasts)
                .build();

        Mockito.when(locationApi.getLocation(Mockito.eq(IP))).thenReturn(location);
        Mockito.when(forecastRepository.findByCountryAndCity(
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(COUNTRY),
                Mockito.eq(CITY)
        )).thenReturn(forecasts);
        Mockito.when(weatherApi.getForecastByIp(Mockito.eq(location))).thenReturn(apiResponse);

        WeatherResult actual = new WeatherService(weatherApi, locationApi, forecastRepository).getMainPageWeather(IP);

        Mockito.verify(forecastRepository).saveAll(Mockito.eq(remoteForecasts));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getWeatherByLocationTest_FromDB_success() {
        for (int i = 0; i < 10; i++) {
            forecasts.add(new Forecast());
        }
        WeatherResult expected = WeatherResult.builder()
                .country(COUNTRY)
                .city(CITY)
                .forecasts(forecasts.subList(0, 2))
                .build();

        Mockito.when(locationApi.getLocation(Mockito.eq(COUNTRY), Mockito.eq(CITY))).thenReturn(location);
        Mockito.when(forecastRepository.findByCountryAndCity(
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(COUNTRY),
                Mockito.eq(CITY)
        )).thenReturn(forecasts.subList(0, 2));
        Mockito.when(weatherApi.getForecastByIp(Mockito.eq(location))).thenReturn(apiResponse);

        WeatherResult actual = new WeatherService(weatherApi, locationApi, forecastRepository)
                .getWeatherByLocation(COUNTRY, CITY, 2);

        Mockito.verifyNoInteractions(weatherApi);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getWeatherByLocationTest_FromDB_failure() {
        for (int i = 0; i < 3; i++) {
            forecasts.add(new Forecast());
        }
        WeatherResult expected = WeatherResult.builder()
                .country(COUNTRY)
                .city(CITY)
                .forecasts(remoteForecasts.subList(0, 5))
                .build();

        Mockito.when(locationApi.getLocation(Mockito.eq(COUNTRY), Mockito.eq(CITY))).thenReturn(location);
        Mockito.when(forecastRepository.findByCountryAndCity(
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(COUNTRY),
                Mockito.eq(CITY)
        )).thenReturn(forecasts);
        Mockito.when(weatherApi.getForecastByIp(Mockito.eq(location))).thenReturn(apiResponse);

        WeatherResult actual = new WeatherService(weatherApi, locationApi, forecastRepository)
                .getWeatherByLocation(COUNTRY, CITY, 5);

        Mockito.verify(forecastRepository).saveAll(Mockito.eq(remoteForecasts));
        Assertions.assertEquals(expected, actual);
    }

    private static void setupMockForecasts() {
        List<ForecastHour> remoteForecastHours = new ArrayList<>();
        for (int i = 0; i < 240; i ++) {
            remoteForecastHours.add(ForecastHour.builder()
                    .hour("0:00")
                    .humidity("-2.60")
                    .airTemperature("-2.6")
                    .pressure(String.format("%.1f", -2.6 / 1000))
                    .build());
            if (remoteForecastHours.size() == 24) {
                remoteForecasts.add(Forecast.builder()
                        .time(LocalDateTime.parse("2018-01-19T00:00:00+00:00",
                                DateTimeFormatter.ISO_DATE_TIME))
                        .city(CITY)
                        .country(COUNTRY)
                        .hourlyWeather(new ArrayList<>(remoteForecastHours))
                        .build());
                remoteForecastHours.clear();
            }
        }
    }

}
