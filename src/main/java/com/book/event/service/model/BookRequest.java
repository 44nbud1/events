package com.book.event.service.model;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5294188737237640015L;

    private String key;

    private List<GroupKey> groupKeys;

    private String userId;
}
