package com.sc.weatherapp.repositories;

import com.sc.weatherapp.model.Forecast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ForecastRepository extends MongoRepository<Forecast, String> {
    @Query("{'country' : ?2, 'city' : ?3, 'time' : {$gte : ?0, $lt : ?1}}")
    List<Forecast> findByCountryAndCity(LocalDateTime startTime, LocalDateTime endTime, String country, String city);

}
