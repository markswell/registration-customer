package com.markswell.exception;

public class NotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "erro.notfound.message";
    }
}
