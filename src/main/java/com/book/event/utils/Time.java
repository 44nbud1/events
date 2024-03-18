package com.book.event.utils;

import com.book.event.exception.EventException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

import static com.book.event.validation.errorcode.EventErrorDescCode.INTERNAL_ERROR;

public class Time {
    public static boolean inRange(Date date, String start,String end) {

        ZoneId jkt = ZoneId.of("Asia/Jakarta");
        Date startDate = null;
        Date endDate = null;
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String d = dateFormat.format(date);

            String startString = d.concat(" ").concat(start);
            String endString = d.concat(" ").concat(end);

            startDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startString);
            endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endString);

            startDate.toInstant().atZone(jkt);
            endDate.toInstant().atZone(jkt);
            date.toInstant().atZone(jkt);

        } catch (Exception e) {
            throw new EventException("Error parse date", INTERNAL_ERROR);
        }

        return date.after(startDate) && endDate.before(date) ;
    }
}
