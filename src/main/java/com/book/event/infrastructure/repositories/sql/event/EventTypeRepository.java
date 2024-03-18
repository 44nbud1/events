package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.event.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Long> { }
