package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByKey(String key);
}
