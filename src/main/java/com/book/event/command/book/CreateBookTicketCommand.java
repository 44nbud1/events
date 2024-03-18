package com.book.event.command.book;

import com.book.event.command.Command;
import com.book.event.service.model.BookRequest;
import com.book.event.service.model.BookResponse;

public interface CreateBookTicketCommand extends Command<BookRequest, BookResponse> {}
