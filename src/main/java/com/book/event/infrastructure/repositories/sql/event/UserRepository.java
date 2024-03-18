package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
