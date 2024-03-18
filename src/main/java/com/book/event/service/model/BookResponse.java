package com.book.event.service.model;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private String bookId;

    private BigDecimal price;

    private String currency;
}
