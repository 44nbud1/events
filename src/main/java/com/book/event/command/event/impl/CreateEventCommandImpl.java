package com.book.event.command.event.impl;

import com.book.event.command.AbstractCommand;
import com.book.event.command.event.CreateEventCommand;
import com.book.event.entity.elastic.event.EventGroup;
import com.book.event.entity.elastic.event.Location;
import com.book.event.entity.sql.event.Event;
import com.book.event.infrastructure.repositories.redis.RedisRepository;
import com.book.event.infrastructure.repositories.sql.event.EventRepository;
import com.book.event.service.event.ElasticService;
import com.book.event.validation.template.ProcessStd;
import com.book.event.validation.template.Template;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateEventCommandImpl extends AbstractCommand<Void, Void> implements CreateEventCommand {

    private ElasticService elasticService;

    private EventRepository eventRepository;

    private RedisRepository redisRepository;
    @Override
    public Void execute(Void unused) {
        Template.processTemplate(new ProcessStd() {

            @Override
            public void checkParameter() {
                // no need for now
            }

            @Override
            public void process() {

                Optional<Event> eventOps = eventRepository.findById(1L);

                if (eventOps.isEmpty()) {
                    throw new RuntimeException("Not found");
                }

                Event event = eventOps.get();

                List<EventGroup> eventGroup = event.getEventGroupSet().stream()
                        .map(eg -> EventGroup.builder()
                                .id(eg.getId())
                                .key(eg.getKey())
                                .category(eg.getCategory())
                                .price(eg.getPrice())
                                .currency("IDR")
                                .startDate(eg.getStartDate())
                                .endDate(eg.getEndDate())
                                .qty(eg.getQty())
                                .build()).toList();

                elasticService.saveAllEventGroup(eventGroup);

                Location location = Location.builder()
                        .venue(event.getLocation().getVenue())
                        .address(event.getLocation().getAddress())
                        .build();

                elasticService.createEvent(com.book.event.entity.elastic.event.Event.builder()
                        .id(event.getId())
                        .name(event.getName())
                        .date(event.getDate())
                        .key(event.getKey())
                        .type(event.getEventType().getName())
                        .location(location)
                        .eventGroups(eventGroup)
                        .build());

                // set quantity
                eventGroup.forEach(e -> redisRepository.createQty(event.getKey()+e.getKey(), e.getQty()));
            }
        });

        return unused;
    }
}
