package com.book.event.infrastructure.repositories.sql.event;

import com.book.event.entity.sql.address.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Location, Long> { }
