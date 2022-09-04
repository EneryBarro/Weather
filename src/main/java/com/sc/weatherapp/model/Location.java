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
public class Location {

    private double latitude;
    private double longitude;
    private String city;
    @JsonProperty("country_name")
    private String country;
}

