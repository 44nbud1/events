package com.book.event.exception;

import com.book.event.transport.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.book.event.validation.errorcode.EventErrorDescCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler {

    @ExceptionHandler({EventException.class})
    public ResponseEntity<HttpResponse<Object>> eventException(EventException e) {

        HttpResponse<Object> response = new HttpResponse<>();

        Map<String, String> resp = new HashMap<>();
        int httpStatus = 0;
         
        if (e.getBaseErrorDesc().getCode().equals(PARAMETER_ERROR.code)) {
            httpStatus = HttpStatus.OK.value();
        } else if (e.getBaseErrorDesc().getCode().equals(DATA_NOT_FOUND.code)) {
            httpStatus = HttpStatus.NOT_FOUND.value();
        } else if (e.getBaseErrorDesc().getCode().equals(LOCK_TIMEOUT.code)) {
            httpStatus = HttpStatus.REQUEST_TIMEOUT.value();
        } else if (e.getBaseErrorDesc().getCode().equals(QTY_SUFFICIENT.code)) {
            httpStatus = HttpStatus.FORBIDDEN.value();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        System.out.println(e.getBaseErrorDesc());

        resp.put("responseMessage", e.getBaseErrorDesc().getMessage());
        response.setData(resp);

        log.error("error response {}, {}", response, e.getBaseErrorDesc());

        return ResponseEntity.status(httpStatus).body(response);
    }

}
