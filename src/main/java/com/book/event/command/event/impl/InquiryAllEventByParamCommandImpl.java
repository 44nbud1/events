package com.book.event.command.event.impl;

import com.book.event.command.AbstractCommand;
import com.book.event.command.event.InquiryAllEventByParamCommand;
import com.book.event.entity.elastic.event.Event;
import com.book.event.service.event.ElasticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InquiryAllEventByParamCommandImpl extends AbstractCommand<String, List<Event>> implements InquiryAllEventByParamCommand {

    private ElasticService elasticService;

    @Override
    public List<Event> execute(String s) {
        return elasticService.inquiryByNameContaining(s);
    }
}
