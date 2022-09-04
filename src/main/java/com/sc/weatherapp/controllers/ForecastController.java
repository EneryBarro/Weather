package com.sc.weatherapp.controllers;

import com.sc.weatherapp.model.Forecast;
import com.sc.weatherapp.services.ForecastService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class ForecastController {

    private ForecastService forecastService;

    @GetMapping("/forecasts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Forecast getForecast(@PathVariable String id) {
        return forecastService.getForecast(id);
    }

    @PostMapping("/forecasts")
    @ResponseStatus(HttpStatus.CREATED)
    public void createForecast(@RequestBody Forecast forecast) {
        forecastService.createForecast(forecast);
    }

    @DeleteMapping("/forecasts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForecast(@PathVariable String id) {
       forecastService.deleteForecast(id);
    }

    @PutMapping("/forecasts/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateForecast(@PathVariable String id, @RequestBody Forecast forecast) {
        forecastService.updateForecast(id, forecast);
    }

}
