package com.book.event.validation.template;

import com.book.event.exception.EventException;
import lombok.extern.slf4j.Slf4j;

import static com.book.event.validation.errorcode.EventErrorDescCode.INTERNAL_ERROR;

@Slf4j
public class Template {

    public static void processTemplate(ProcessStd std) {
        try {
            std.checkParameter();
            std.process();
        } catch (EventException ex) {
            log.warn("Event service process, occur exception: " + ex.getMessage());
            log.warn("Error" , ex);
        } catch (Throwable throwable) {
            log.error( throwable.getMessage());
            log.error("occur unknown exception {}", throwable.getMessage());

            throw new EventException("occur unknown exception", INTERNAL_ERROR);
        }
    }

}