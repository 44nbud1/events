package com.book.event.service.event.impl;

import com.book.event.entity.elastic.event.Event;
import com.book.event.entity.elastic.event.EventGroup;
import com.book.event.infrastructure.repositories.elastic.event.EventElasticRepository;
import com.book.event.infrastructure.repositories.elastic.event.EventGroupElasticRepository;
import com.book.event.service.event.ElasticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElasticServiceImpl implements ElasticService {

    private EventElasticRepository eventRepository;

    private EventGroupElasticRepository eventGroupRepository;

    @Override
    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Event inquiryByKey(String key) {
        return eventRepository.findByKey(key);
    }

    @Override
    public List<Event> inquiryByNameContaining(String name) {

        return eventRepository.findByNameContaining(name);
    }

    @Override
    public void saveAllEventGroup(final List<EventGroup> eventGroups) {
        eventGroupRepository.saveAll(eventGroups);
    }
}
