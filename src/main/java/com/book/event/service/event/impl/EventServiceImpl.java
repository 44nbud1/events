package com.book.event.service.event.impl;

import com.book.event.entity.elastic.event.Event;
import com.book.event.service.event.ElasticService;
import com.book.event.service.event.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private ElasticService elasticService;

    @Override
    public List<Event> InquiryAllEventByName(String name) {

        return elasticService.inquiryByNameContaining(name);
    }
}
