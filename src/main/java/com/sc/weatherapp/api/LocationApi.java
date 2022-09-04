package com.sc.weatherapp.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.sc.weatherapp.model.Location;

import java.math.BigDecimal;


@Component
@ConfigurationProperties(prefix = "location")
public class LocationApi {

    @Value("${location.api.url}")
    private String LOCATION_API_URL;

    @Value("${location.api.key}")
    private String LOCATION_API_KEY;

    @Value("${location.api-position-stack.url}")
    private String LOCATION_POSITION_STACK_API_URL;

    @Value("${location.api-position-stack.key}")
    private String LOCATION_POSITION_STACK_API_KEY;

    private final RestTemplate restTemplate;

    public LocationApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Location getLocation(String ip) {
        return  restTemplate.getForObject(LOCATION_API_URL + ip + "?access_key=" + LOCATION_API_KEY, Location.class);
    }

    public Location getLocation(String country, String city){
        String response = restTemplate.getForObject(LOCATION_POSITION_STACK_API_URL + "forward" + "?access_key="
                        + LOCATION_POSITION_STACK_API_KEY + "&query=" + country + ", " + city + "&limit=1",
                String.class);

        JSONObject responseJson = ((JSONArray) new JSONObject(response).get("data")).getJSONObject(0);
        double latitude = ((BigDecimal) responseJson.get("latitude")).doubleValue();
        double longitude = ((BigDecimal) responseJson.get("longitude")).doubleValue();
        String cityRegion = (String) responseJson.get("region");
        String countryName = (String) responseJson.get("country");

        return Location.builder()
                .latitude(latitude)
                .longitude(longitude)
                .city(cityRegion)
                .country(countryName)
                .build();
    }
}