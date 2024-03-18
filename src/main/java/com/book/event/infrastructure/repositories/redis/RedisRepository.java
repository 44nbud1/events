package com.book.event.infrastructure.repositories.redis;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {

    Object inquiryQty(String key);

    void createEvent(String key, Object value);

    void createEventWithTTL(String key, Object value, Long ttl);

    Long incrementQty(String key, Integer qty) ;

    Long decrementQty(String key, Integer qty) ;

    void createQty(String key, Object value);
}
