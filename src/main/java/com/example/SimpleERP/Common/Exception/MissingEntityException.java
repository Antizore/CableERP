package com.example.SimpleERP.Common.Exception;

public class MissingEntityException extends RuntimeException {
    public MissingEntityException(String message) {
        super(message);
    }
}
