package com.book.event.transport.http.v1.even.controller;

import com.book.event.command.event.CreateEventCommand;
import com.book.event.command.event.InquiryAllEventByParamCommand;
import com.book.event.entity.elastic.event.Event;
import com.book.event.transport.http.HttpResponse;
import com.book.event.transport.http.v1.event.controller.EventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventControllerTest {

    @Mock
    private CreateEventCommand createEventCommand;

    @Mock
    private InquiryAllEventByParamCommand inquiryAllEventByParamCommand;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create() {
        // When
        HttpResponse<Object> response = eventController.create();

        // Then
        assertEquals("Success", response.getData());
        verify(createEventCommand).execute(null);
    }

    @Test
    void findById() {
        // Given
        String name = "test";
        List<Event> expectedEvents = List.of();
        when(inquiryAllEventByParamCommand.execute(name)).thenReturn(expectedEvents);

        // When
        HttpResponse<List<Event>> response = eventController.findById(name);

        // Then
        assertEquals(expectedEvents, response.getData());
        verify(inquiryAllEventByParamCommand).execute(name);
    }
}
