package com.hyunbenny.okkydokky.exception;

public class PostNotFoundException extends RuntimeException{

    private static final String MESSAGE = "게시글이 존재하지 않습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }

    public PostNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public int getStatusCode() {
        return 404;
    }
}
