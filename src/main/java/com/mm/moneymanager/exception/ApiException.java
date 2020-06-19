package com.mm.moneymanager.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
    @JsonIgnore
    private final ZonedDateTime timestamp;
}
