package com.mm.moneymanager.exception;

public class ApiBadRequestException extends RuntimeException{
    public ApiBadRequestException(String message) {
        super(message);
    }
}
