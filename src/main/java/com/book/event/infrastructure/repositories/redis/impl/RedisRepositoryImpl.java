package com.book.event.infrastructure.repositories.redis.impl;

import com.book.event.infrastructure.repositories.redis.RedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@AllArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final String hashReference= "Event";

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object inquiryQty(String key) {

        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void createEvent(String key, Object value) {

        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void createEventWithTTL(String key, Object value, Long ttl) {
        createEvent(key, value);
        redisTemplate.boundValueOps(key).expire(ttl, TimeUnit.SECONDS);
    }

    @Override
    public Long incrementQty(String key, Integer qty) {
        return redisTemplate.opsForValue().increment(key, qty);
    }

    @Override
    public Long decrementQty(String key, Integer qty) {
        return redisTemplate.opsForValue().increment(key, -qty);
    }

    @Override
    public void createQty(String key, Object value) {

        redisTemplate.opsForValue().set(key,value);
    }

}
