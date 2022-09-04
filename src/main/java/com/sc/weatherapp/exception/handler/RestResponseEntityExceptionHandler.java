package com.sc.weatherapp.exception.handler;

import com.sc.weatherapp.exception.ForecastNotFoundException;
import com.sc.weatherapp.exception.UserNotFoundException;
import com.sc.weatherapp.exception.WeatherException;
import com.sc.weatherapp.exception.model.ApiExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WeatherException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handleWeatherException(RuntimeException ex, WebRequest request) {
        return new ApiExceptionModel(ex.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiExceptionModel handleUserNotFoundException(RuntimeException ex, WebRequest request) {
        return new ApiExceptionModel(ex.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }

    @ExceptionHandler(ForecastNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiExceptionModel handleForecastNotFoundException(RuntimeException ex, WebRequest request) {
        return new ApiExceptionModel(ex.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }
}
