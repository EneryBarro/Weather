package com.sc.weatherapp.services;

import com.sc.weatherapp.exception.ForecastNotFoundException;
import com.sc.weatherapp.model.Forecast;
import com.sc.weatherapp.repositories.ForecastRepository;
import com.sc.weatherapp.utils.uuid.ForecastIdHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
@Service
@AllArgsConstructor
public class ForecastService {

    public static final String ERROR_TEMPLATE = "Forecast with id: %s not found!";
    private ForecastRepository forecastRepository;

    public void createForecast(Forecast forecast) {
        LocalDateTime timestamp = LocalDateTime.now();
        forecast.setTime(timestamp);
        String id = ForecastIdHelper.generateId(forecast);
        log.info("Create forecast: '{}'", id);
        forecast.setId(id);
        forecastRepository.save(forecast);
    }

    public Forecast getForecast(String id) {
        log.info("Get forecast: '{}'", id);

        return forecastRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format(ERROR_TEMPLATE, id));
                    throw new ForecastNotFoundException(String.format(ERROR_TEMPLATE, id));
                });
    }

    public void deleteForecast(String id) {
        log.info("Delete forecast: '{}'", id);
        if (!forecastRepository.existsById(id)) {
            log.error(String.format(ERROR_TEMPLATE, id));
            throw new ForecastNotFoundException(String.format(ERROR_TEMPLATE, id));
        }
        forecastRepository.deleteById(id);
    }

    public void updateForecast(String id, Forecast forecast) {
        log.info("Update forecast: '{}'", id);
        if (!forecastRepository.existsById(id)) {
            log.error(String.format(ERROR_TEMPLATE, id));
            throw new ForecastNotFoundException(String.format(ERROR_TEMPLATE, id));
        }
        Forecast oldForecast = getForecast(id);
        forecastRepository.save(Forecast.builder()
                        .id(id)
                        .country(Objects.isNull(forecast.getCountry()) ? oldForecast.getCountry() : forecast.getCountry())
                        .city(Objects.isNull(forecast.getCity()) ? oldForecast.getCity() : forecast.getCity())
                        .time(Objects.isNull(forecast.getTime()) ? oldForecast.getTime() : forecast.getTime())
                        .hourlyWeather(Objects.isNull(forecast.getHourlyWeather()) ? oldForecast.getHourlyWeather() : forecast.getHourlyWeather())
                        .build());
    }

}
