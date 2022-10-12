package com.hyunbenny.okkydokky.exception;

public class UserNotExistException extends RuntimeException {

    private static String MESSAGE = "존재하지 않는 유저입니다. userId : ";

    public UserNotExistException(String userId) {
        super(MESSAGE + userId);
    }

    public UserNotExistException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public int getStatusCode() {
        return 404;
    }
}
