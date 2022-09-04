package com.sc.weatherapp.controllers;

import com.sc.weatherapp.model.WeatherResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sc.weatherapp.services.WeatherService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class WeatherController {

    private WeatherService weatherService;

    @GetMapping("/weather/{ip}")
    @ResponseStatus(HttpStatus.OK)
    public WeatherResult getWeather(@PathVariable String ip) {
        return weatherService.getMainPageWeather(ip);
    }

    @GetMapping("/weather/main")
    @ResponseStatus(HttpStatus.OK)
    public WeatherResult getWeatherMain(@RequestHeader("IP-Address") String ip) {
        return weatherService.getMainPageWeather(ip);
    }

    @GetMapping("/weather/{country}/{city}/{days}")
    @ResponseStatus(HttpStatus.OK)
    public WeatherResult getWeather(@PathVariable String country, @PathVariable String city, @PathVariable int days) {
        return weatherService.getWeatherByLocation(country, city, days);
    }

}
