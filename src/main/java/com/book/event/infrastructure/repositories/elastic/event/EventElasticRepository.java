package com.book.event.infrastructure.repositories.elastic.event;

import com.book.event.entity.elastic.event.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventElasticRepository extends ElasticsearchRepository<Event, Long>{
    List<Event> findByNameContaining(String Long);

    Event findByKey(String key);
}
