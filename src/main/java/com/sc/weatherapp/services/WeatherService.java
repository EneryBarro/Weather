package com.sc.weatherapp.services;


import com.sc.weatherapp.api.LocationApi;
import com.sc.weatherapp.api.WeatherApi;
import com.sc.weatherapp.model.Forecast;
import com.sc.weatherapp.model.Location;
import com.sc.weatherapp.model.WeatherResult;
import com.sc.weatherapp.utils.mappers.ForecastMapper;
import com.sc.weatherapp.repositories.ForecastRepository;
import com.sc.weatherapp.utils.uuid.ForecastIdHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class WeatherService {

    private WeatherApi weatherApi;

    private LocationApi locationApi;

    private ForecastRepository forecastRepository;

    public WeatherResult getMainPageWeather(String ip) {
        log.info("Getting weather forecast by - {}.", ip);
        Location location = locationApi.getLocation(ip);
        return getWeatherByCoordinates(location, 10);
    }

    public WeatherResult getWeatherByLocation(String county, String city, int days) {
        log.info("Getting weather forecast by - {}, {}, {}.", county, city, days);
        Location location = locationApi.getLocation(county,city);
        return getWeatherByCoordinates(location, days);
    }

    private WeatherResult getWeatherByCoordinates(Location location, int days) {
        days = Math.min(days, 10);
        Calendar calendar = Calendar.getInstance();
        List<Forecast> resultForecast = forecastRepository.findByCountryAndCity(
                LocalDateTime.of(calendar.get(Calendar.YEAR),
                                 calendar.get(Calendar.MONTH) + 1,
                                 calendar.get(Calendar.DAY_OF_MONTH), 0, 0),
                LocalDateTime.of(calendar.get(Calendar.YEAR),
                                 calendar.get(Calendar.MONTH) + 1,
                                 calendar.get(Calendar.DAY_OF_MONTH), 0, 0).plusDays(days),
                location.getCountry(),
                location.getCity()
        );
        if (resultForecast.size() < days) {
            resultForecast = ForecastMapper.mapToForecast(weatherApi.getForecastByIp(location));
            resultForecast = resultForecast.stream()
                    .map(forecast -> addLocation(forecast, location))
                    .map(ForecastIdHelper::addId)
                    .collect(Collectors.toList());
            forecastRepository.saveAll(resultForecast);
        }

        return WeatherResult.builder()
                .country(location.getCountry())
                .city(location.getCity())
                .forecasts(resultForecast.subList(0, days))
                .build();
    }

    private Forecast addLocation(Forecast forecast, Location location) {
        forecast.setCity(location.getCity());
        forecast.setCountry(location.getCountry());
        return forecast;
    }

}
