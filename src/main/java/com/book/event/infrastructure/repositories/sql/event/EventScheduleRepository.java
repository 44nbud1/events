package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.event.Event;
import com.book.event.entity.sql.event.EventSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventScheduleRepository extends JpaRepository<EventSchedule, Long> {
    EventSchedule findByEvent(Event event);
}
