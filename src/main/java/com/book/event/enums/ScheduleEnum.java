package com.book.event.enums;

import lombok.Getter;

@Getter
public enum ScheduleEnum {

    Sequences( 1000,"sequence", "13:00:00", "15:00:00"),

    UNKNOWN( 0,"", "", "")
    ;

    private final Integer max;

    private final String type;

    private final String start;

    private final String end;

    ScheduleEnum(Integer totalOfData, String type, String start, String end) {
        this.max = totalOfData;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public static ScheduleEnum getSchedule(String s) {

        for (ScheduleEnum configEnum : values()) {
            if (configEnum.getType().equals(s)) {
                return configEnum;
            }
        }
        return ScheduleEnum.UNKNOWN;
    }


}
