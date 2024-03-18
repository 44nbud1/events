package com.book.event.entity.elastic.event;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Data
@Document(indexName = "event_location_index")
public class Location {

    @Field(type = FieldType.Text)
    private String venue;

    @Field(type = FieldType.Text)
    private String address;
}
