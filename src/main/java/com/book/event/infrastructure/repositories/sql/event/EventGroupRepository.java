package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.event.EventGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventGroupRepository extends JpaRepository<EventGroup, Long> {

    EventGroup findByKey(String key);
}
