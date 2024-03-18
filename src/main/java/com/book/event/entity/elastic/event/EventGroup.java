package com.book.event.entity.elastic.event;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "event_group_index")
public class EventGroup {

    @Id
    @Field(type = FieldType.Integer)
    private Long id;

    @Field(type = FieldType.Text)
    private String key;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Float)
    private BigDecimal price;

    @Field(type = FieldType.Text)
    private String currency;

    @Field(type = FieldType.Date)
    private Date startDate;

    @Field(type = FieldType.Date)
    private Date endDate;

    @Field(type = FieldType.Integer)
    private Integer qty;

}
