package org.crcaguilerapo.paymentPlatform.domain.exceptions;

public enum ErrorCode {
    REPORTED_FOR_FRAUD("REPORTED_FOR_FRAUD"),
    UNKNOWN("UNKNOWN");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}