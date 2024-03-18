package com.book.event.service.event;

import com.book.event.entity.elastic.event.Event;
import com.book.event.entity.elastic.event.EventGroup;
import com.book.event.infrastructure.repositories.elastic.event.EventElasticRepository;
import com.book.event.infrastructure.repositories.elastic.event.EventGroupElasticRepository;
import com.book.event.service.event.impl.ElasticServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ElasticServiceImplTest {

    @Mock
    private EventElasticRepository eventRepository;

    @Mock
    private EventGroupElasticRepository eventGroupRepository;

    @InjectMocks
    private ElasticServiceImpl elasticService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createEvent() {
        // Given
        Event event = new Event();

        // When
        elasticService.createEvent(event);

        // Then
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void inquiryByKey() {
        // Given
        String key = "TestKey";
        Event expectedEvent = new Event();
        when(eventRepository.findByKey(any())).thenReturn(expectedEvent);

        // When
        Event actualEvent = elasticService.inquiryByKey(key);

        // Then
        assertEquals(expectedEvent, actualEvent);
        verify(eventRepository, times(1)).findByKey(key);
    }

    @Test
    void inquiryByNameContaining() {
        // Given
        String name = "TestName";
        List<Event> expectedEvents = Collections.singletonList(new Event());
        when(eventRepository.findByNameContaining(any())).thenReturn(expectedEvents);

        // When
        List<Event> actualEvents = elasticService.inquiryByNameContaining(name);

        // Then
        assertEquals(expectedEvents, actualEvents);
        verify(eventRepository, times(1)).findByNameContaining(name);
    }

    @Test
    void saveAllEventGroup() {
        // Given
        List<EventGroup> eventGroups = Collections.singletonList(new EventGroup());

        // When
        elasticService.saveAllEventGroup(eventGroups);

        // Then
        verify(eventGroupRepository, times(1)).saveAll(eventGroups);
    }
}