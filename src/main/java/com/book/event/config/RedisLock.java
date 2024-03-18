package com.book.event.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLock {

    private static final String LOCK_KEY = "app-lock";
    private RLock lock;
    private final RedissonClient redisson;

    public void init(String l) {
        lock = redisson.getLock(l);
    }

    public boolean tryLock() {

        //tryLock(long time, TimeUnit unit)
        return lock.tryLock();
    }

    public void unlock() {
        lock.unlock();
    }

}