package com.example.CableERP.Common.Exception;

public class CannotDeleteComponentActiveInBOM extends RuntimeException {
    public CannotDeleteComponentActiveInBOM(String message) {
        super(message);
    }
}
