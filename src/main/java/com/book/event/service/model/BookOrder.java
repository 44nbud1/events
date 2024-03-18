package com.book.event.service.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@RedisHash
public class BookOrder implements Serializable {

    private String Key;

    private String userId;

    private BigDecimal totalPrice;

    List<GroupKey> groupKeys;

}
