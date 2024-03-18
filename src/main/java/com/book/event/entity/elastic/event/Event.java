package com.book.event.entity.elastic.event;

import co.elastic.clients.util.DateTime;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "event_index")
public class Event {
    @Id
    @Field(type = FieldType.Integer)
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String key;

    private Location location;

    @Field(type = FieldType.Date)
    private Date date;

    @Field(type = FieldType.Keyword)
    private String type;

    private List<EventGroup> eventGroups;
}
