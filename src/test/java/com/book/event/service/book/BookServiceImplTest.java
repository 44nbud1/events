package com.book.event.service.book;

import com.book.event.config.RedisLock;
import com.book.event.entity.sql.event.Event;
import com.book.event.entity.sql.event.EventGroup;
import com.book.event.entity.sql.event.EventSchedule;
import com.book.event.exception.EventException;
import com.book.event.infrastructure.repositories.redis.RedisRepository;
import com.book.event.infrastructure.repositories.sql.event.EventRepository;
import com.book.event.infrastructure.repositories.sql.event.EventScheduleRepository;
import com.book.event.service.book.impl.BookServiceImpl;
import com.book.event.service.event.ElasticService;
import com.book.event.service.model.BookRequest;
import com.book.event.service.model.BookResponse;
import com.book.event.service.model.GroupKey;
import com.book.event.transport.queue.producer.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventScheduleRepository eventScheduleRepository;

    @Mock
    private Producer messageProducer;

    @Mock
    private ElasticService elasticService;

    @Mock
    private RedisLock redisLock;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService.init(); // initialize RedisLock
    }

    @Test
    void createBook_Success() {
        // Given
        BookRequest bookRequest = new BookRequest();
        bookRequest.setKey("TestKey");
        bookRequest.setUserId("TestUserId");
        List<GroupKey> groupKeys = new ArrayList<>();
        groupKeys.add(new GroupKey("TestGroupKey", "234",1, 1L, BigDecimal.TEN));
        bookRequest.setGroupKeys(groupKeys);

        Event event = new Event();
        EventGroup eventGroup = new EventGroup();
        eventGroup.setKey("TestGroupKey");
        eventGroup.setQty(10);
        eventGroup.setPrice(BigDecimal.TEN);
        event.setEventGroupSet(Collections.singleton(eventGroup));

        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.setLimitRatioType("sequence");
        when(eventRepository.findByKey(any())).thenReturn(event);
        when(eventScheduleRepository.findByEvent(any())).thenReturn(eventSchedule);
        when(redisLock.tryLock()).thenReturn(true);
        when(redisRepository.inquiryQty(any())).thenReturn("10");
        when(redisRepository.decrementQty(any(), anyInt())).thenReturn(1L);
        when(elasticService.inquiryByKey(any())).thenReturn(com.book.event.entity.elastic.event.Event.builder().build());
        messageProducer.sendMessage(any(), any());

        // When
        BookResponse bookResponse = bookService.createBook(bookRequest);

        // Then
        assertNotNull(bookResponse);
        verify(messageProducer, times(2)).sendMessage(any(), any());
    }

    @Test
    void createBook_EventNotFound() {
        // Given
        BookRequest bookRequest = new BookRequest();
        bookRequest.setKey("NonExistentKey");

        when(eventRepository.findByKey(any())).thenReturn(null);

        // When/Then
        assertThrows(EventException.class, () -> bookService.createBook(bookRequest));
    }

    @Test
    void createBook_ScheduleNotFound() {
        // Given
        BookRequest bookRequest = new BookRequest();
        bookRequest.setKey("TestKey");
        Event event = new Event();
        when(eventRepository.findByKey(any())).thenReturn(event);
        when(eventScheduleRepository.findByEvent(any())).thenReturn(null);

        // When/Then
        assertThrows(NullPointerException.class, () -> bookService.createBook(bookRequest));
    }
}
