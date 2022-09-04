package com.sc.weatherapp.utils.uuid;

import com.sc.weatherapp.model.Forecast;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@UtilityClass
public class ForecastIdHelper {
    public static String generateId(Forecast forecast) {
        return UUID.nameUUIDFromBytes(String.format("%s.%s.%s",
                        forecast.getCountry(),
                        forecast.getCity(),
                        forecast.getTime().toString())
                        .getBytes(StandardCharsets.UTF_8))

                .toString();
    }

    public static Forecast addId(Forecast forecast) {
        forecast.setId(generateId(forecast));
        return forecast;
    }
}
