package com.alanbuttars.resolut.backend.models;

/**
 * Simple POJO representing an error message.
 */
public class Error {

    private final String errorMessage;

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
