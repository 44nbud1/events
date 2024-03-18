package com.book.event.infrastructure.repositories.elastic.event;

import com.book.event.entity.elastic.event.EventGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface EventGroupElasticRepository extends ElasticsearchRepository<EventGroup, Long> {
    Optional<EventGroup> findByKey(String key);
}
