package com.book.event.exception;

import com.book.event.validation.errorcode.BaseErrorDesc;

public abstract class BaseException extends RuntimeException {

    public abstract void setEventErrorDec(BaseErrorDesc errorDec);

    BaseException(String message) {
        super(message);
    }
    
}