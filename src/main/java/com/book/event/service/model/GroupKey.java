package com.book.event.service.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupKey {

    private String key;

    private String redisKey;

    private Integer qty;

    private Long id;

    private BigDecimal price;

    public BigDecimal totalPrice() {

        BigDecimal totalPrice = BigDecimal.ZERO;

        return totalPrice.add(this.price.multiply(BigDecimal.valueOf(this.qty)));

    }
}
