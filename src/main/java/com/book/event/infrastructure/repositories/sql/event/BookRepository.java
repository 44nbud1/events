package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByKey(String key);
}
