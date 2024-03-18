package com.book.event.exception;


import com.book.event.validation.errorcode.BaseErrorDesc;
import lombok.Getter;

@Getter
public class EventException extends BaseException{

    private BaseErrorDesc baseErrorDesc;

    public EventException(String message, BaseErrorDesc baseErrorDesc) {
        super(message);
        this.baseErrorDesc = baseErrorDesc;
    }

    @Override
    public void setEventErrorDec(final BaseErrorDesc errorDec) {
        this.baseErrorDesc = new BaseErrorDesc() {
            @Override
            public String getCode() {
                return errorDec.getCode();
            }

            @Override
            public String getMessage() {
                return errorDec.getMessage();
            }

            @Override
            public String getErrorLevel() {
                return errorDec.getErrorLevel();
            }
        };
    }
}