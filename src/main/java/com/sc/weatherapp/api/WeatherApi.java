package com.sc.weatherapp.api;

import com.sc.weatherapp.model.Location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "weather")
public class WeatherApi {

    @Value("${weather.api.url}")
    private String WEATHER_API_URL;

    @Value("${weather.api.key}")
    public String WEATHER_API_KEY;

    private static final List<String> WEATHER_PARAMS = List.of("airTemperature", "pressure", "humidity");

    private final RestTemplate restTemplate;

    public WeatherApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getForecastByIp(Location location) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", WEATHER_API_KEY);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        String urlTemplate = UriComponentsBuilder
                .fromHttpUrl(WEATHER_API_URL)
                .queryParam("lng", location.getLongitude())
                .queryParam("lat", location.getLatitude())
                .queryParam("source", "sg")
                .queryParam("params", WEATHER_PARAMS.stream()
                        .reduce((accum, param) -> accum.isEmpty()
                                ? param
                                : accum + ',' + param))
                .encode().toUriString();

        return restTemplate.exchange(urlTemplate, HttpMethod.GET, httpEntity, String.class)
                .getBody();

    }
}
