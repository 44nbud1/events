package com.book.event.command.event;

import com.book.event.command.Command;
import com.book.event.entity.elastic.event.Event;

import java.util.List;

public interface InquiryAllEventByParamCommand extends Command<String, List<Event>> {}