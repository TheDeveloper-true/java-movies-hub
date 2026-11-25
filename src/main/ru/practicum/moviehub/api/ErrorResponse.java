package ru.practicum.moviehub.api;

public class ErrorResponse {
    private final String error;
    private final String details;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.details = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return details;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", message='" + details + '\'' +
                '}';
    }
}