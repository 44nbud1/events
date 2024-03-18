package com.book.event.command.book.impl;

import com.book.event.command.AbstractCommand;
import com.book.event.command.book.CreateBookTicketCommand;
import com.book.event.exception.InternalTimeoutException;
import com.book.event.service.book.BookService;
import com.book.event.service.model.BookRequest;
import com.book.event.service.model.BookResponse;
import com.book.event.utils.GenericContainer;
import com.book.event.validation.template.ProcessStd;
import com.book.event.validation.template.Template;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CreateBookTicketCommandImpl extends AbstractCommand<BookRequest, BookResponse> implements CreateBookTicketCommand {

    private BookService service;

    public BookResponse execute(BookRequest bookRequest)  {

        GenericContainer<BookResponse> bookResponseGenericContainer = new GenericContainer<>();

        Template.processTemplate(new ProcessStd() {

            @Override
            public void checkParameter() {
                // no need for now
            }

            public void process() {
                bookResponseGenericContainer.setValue(
                        service.createBook(bookRequest)
                );
            }
        });

        return bookResponseGenericContainer.getValue();
    }

}
