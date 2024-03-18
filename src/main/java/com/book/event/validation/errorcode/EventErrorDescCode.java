package com.book.event.validation.errorcode;

public enum EventErrorDescCode implements BaseErrorDesc {

    PARAMETER_ERROR("001", "Parameter is error", ""),
    DATA_NOT_FOUND("002", "Data is not found", ""),
    LOCK_TIMEOUT("003", "Lock timeout", ""),
    QTY_SUFFICIENT("004", "Lock timeout", ""),
    INTERNAL_ERROR("999", "Internal error", ""),

    ;

    public final String code;

    public final String message;

    public final String errorLevel;

    EventErrorDescCode(String code, String message, String errorLevel) {
        this.code = code;
        this.message = message;
        this.errorLevel = errorLevel;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getErrorLevel() {
        return errorLevel;
    }

}
