package com.book.event.command;

public interface Command<REQUEST, RESPONSE> {
  RESPONSE execute(REQUEST request);

}
