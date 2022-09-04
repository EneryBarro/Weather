package com.sc.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
@CompoundIndexes({
    @CompoundIndex(unique = true, def = "{'country': 1, 'city': 1, 'time': 1}", name = "forecastIndex")
})
public class Forecast {

    @Id
    @JsonIgnore
    String id;
    @JsonProperty("Country")
    String country;
    @JsonProperty("City")
    String city;
    @JsonProperty("Date")
    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy MMM dd")
    LocalDateTime time;
    @JsonProperty("Hourly weather")
    List<ForecastHour> hourlyWeather;

}


