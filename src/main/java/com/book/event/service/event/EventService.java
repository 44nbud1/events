package com.book.event.service.event;

import com.book.event.entity.elastic.event.Event;

import java.util.List;

public interface EventService {
    List<Event> InquiryAllEventByName(String name);
}
