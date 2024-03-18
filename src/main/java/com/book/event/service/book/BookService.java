package com.book.event.service.book;

import com.book.event.entity.elastic.event.Event;
import com.book.event.service.model.BookRequest;
import com.book.event.service.model.BookResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BookService {

    BookResponse createBook(BookRequest bookRequest);
}
