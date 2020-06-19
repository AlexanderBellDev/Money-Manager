package com.mm.moneymanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {
    ZoneId zoneId = ZoneId.of("Europe/London");

    @ExceptionHandler(value = {ApiBadRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiBadRequestException apiBadRequestException){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(apiBadRequestException.getMessage(), badRequest, ZonedDateTime.now(zoneId));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {ApiNotFoundException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiNotFoundException apiNotFoundException){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(apiNotFoundException.getMessage(), httpStatus, ZonedDateTime.now(zoneId));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {ApiNoContentException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiNoContentException apiNoContentException){
        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
        ApiException apiException = new ApiException(apiNoContentException.getMessage(), httpStatus, ZonedDateTime.now(zoneId));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiException apiException = new ApiException(errors.toString(),httpStatus,ZonedDateTime.now(zoneId));
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
