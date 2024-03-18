package com.book.event.transport.http.v1.book.controller;

import com.book.event.command.book.CreateBookTicketCommand;
import com.book.event.service.model.BookResponse;
import com.book.event.service.model.GroupKey;
import com.book.event.transport.http.HttpResponse;
import com.book.event.transport.http.v1.book.dto.BookRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/books")
@AllArgsConstructor
public class BookController {

    private CreateBookTicketCommand createBookTicketCommand;

    @PostMapping
    public HttpResponse<BookResponse> createBook(@RequestBody BookRequest bookRequest) throws JsonProcessingException {

        BookResponse res = createBookTicketCommand.execute(com.book.event.service.model.BookRequest.builder()
                        .key(bookRequest.getKey())
                .groupKeys(bookRequest.getGroupKeys().stream().map(b -> GroupKey.builder()
                            .key(b.getKey())
                            .qty(b.getQty())
                        .build()).collect(Collectors.toList()))
                .build());

        return HttpResponse.<BookResponse>builder().data(res).build();
    }
}
