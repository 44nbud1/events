package com.book.event.transport.http.v1.event.controller;

import com.book.event.command.event.CreateEventCommand;
import com.book.event.command.event.InquiryAllEventByParamCommand;
import com.book.event.transport.http.HttpResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/events")
@AllArgsConstructor
public class EventController {

    private CreateEventCommand createEventCommand;

    private InquiryAllEventByParamCommand inquiryAllEventByParamCommand;

    @PostMapping
    public HttpResponse<Object> create() {

        // create new events
        createEventCommand.execute(null);

        return HttpResponse.<Object>builder()
                .data("Success")
                .build();
    }

    @GetMapping
    public HttpResponse<List<com.book.event.entity.elastic.event.Event>> findById(@RequestParam String name) {
        return HttpResponse.<List<com.book.event.entity.elastic.event.Event>>builder().
                data(inquiryAllEventByParamCommand.execute(name)).build();
    }

}

