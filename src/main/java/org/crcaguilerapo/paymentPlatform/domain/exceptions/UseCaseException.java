package org.crcaguilerapo.paymentPlatform.domain.exceptions;

public class UseCaseException extends Exception {

    public ErrorCode errorCode;

    public UseCaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}