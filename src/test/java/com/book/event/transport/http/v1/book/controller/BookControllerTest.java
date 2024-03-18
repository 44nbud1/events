package com.book.event.transport.http.v1.book.controller;

import com.book.event.command.book.CreateBookTicketCommand;
import com.book.event.service.model.BookResponse;
import com.book.event.transport.http.HttpResponse;
import com.book.event.transport.http.v1.book.dto.BookRequest;
import com.book.event.transport.http.v1.book.dto.GroupKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private CreateBookTicketCommand createBookTicketCommand;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createBook_Success() throws JsonProcessingException {
        // Given
        BookRequest bookRequest = new BookRequest();
        bookRequest.setKey("TestKey");
        List<GroupKey> groupKeys = Collections.singletonList(new GroupKey("GroupKey", 1));
        bookRequest.setGroupKeys(groupKeys);

        BookResponse expectedResponse = new BookResponse();
        expectedResponse.setBookId("TestKey");

        when(createBookTicketCommand.execute(any())).thenReturn(expectedResponse);

        // When
        HttpResponse<BookResponse> httpResponse = bookController.createBook(bookRequest);

        // Then
        assertEquals(expectedResponse, httpResponse.getData());
        verify(createBookTicketCommand, times(1)).execute(any());
    }

}
