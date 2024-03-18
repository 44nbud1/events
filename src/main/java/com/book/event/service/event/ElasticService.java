package com.book.event.service.event;

import com.book.event.entity.elastic.event.Event;
import com.book.event.entity.elastic.event.EventGroup;

import java.util.List;

public interface ElasticService {
    void createEvent(final Event event);

    Event inquiryByKey(final String key);

    List<Event> inquiryByNameContaining(final String name);

    void saveAllEventGroup(final List<EventGroup> eventGroups);
}
