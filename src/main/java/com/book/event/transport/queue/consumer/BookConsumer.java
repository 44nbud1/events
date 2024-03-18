package com.book.event.transport.queue.consumer;

import com.book.event.entity.elastic.event.Event;
import com.book.event.entity.sql.book.Book;
import com.book.event.entity.sql.user.User;
import com.book.event.infrastructure.repositories.redis.RedisRepository;
import com.book.event.infrastructure.repositories.sql.event.BookRepository;
import com.book.event.service.event.ElasticService;
import com.book.event.service.model.BookOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BookConsumer {

    private ElasticService elasticService;

    private BookRepository bookRepository;

    private RedisRepository redisRepository;

    @KafkaListener(topics = "Book_Created_Topic", groupId = "my-group-id")
    public void listen(String message) {
        common(message,false);
    }

    @KafkaListener(topics = "Book_Rollback_Topic", groupId = "my-group-id")
    public void listenRollback(String msg) {

        common(msg,true);
    }

    private void common(String message, boolean isRollback) {

        log.info("Message process {} : action isRollack: {}", message , isRollback);

        BookOrder bookOrder = null;

        try {
            bookOrder = toClass(message);
        } catch (Exception e) {

            throw new RuntimeException("error to parse");
        }

        BookOrder finalBookOrder = bookOrder;
        bookOrder.getGroupKeys().forEach(s -> {

            // temp query
            List<Event> events = elasticService.inquiryByNameContaining("bli");
            events.forEach(event -> {
                event.getEventGroups().forEach(eventGroup -> {
                    if (s.getId().equals(eventGroup.getId())) {

                        if (isRollback) {

                            eventGroup.setQty(eventGroup.getQty() + s.getQty());

                        } else {
                            eventGroup.setQty(eventGroup.getQty() - s.getQty());
                        }

                        elasticService.createEvent(event);
                    }
                });
            });
        });

        Book book = null;

        if (isRollback) {
            Book existBook = bookRepository.findByKey(finalBookOrder.getKey());

            existBook.setStatus("CANCELLED");
            book = existBook;

        } else {

            book = Book.builder()
                    .key(finalBookOrder.getKey())
                    .price(finalBookOrder.getTotalPrice())
                    .user(User.builder()
                            .id(1L)
                            .build())
                    .status("INITIATED")
                    .build();
        }

       bookRepository.save(book);

        if (isRollback) {
            bookOrder.getGroupKeys().forEach(s -> {

                redisRepository.incrementQty(
                        s.getRedisKey(),  s.getQty()
                );
            });
        }

        log.info("Message process Success to process");

    }

    private BookOrder toClass(String s) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(s, BookOrder.class);
    }
}

