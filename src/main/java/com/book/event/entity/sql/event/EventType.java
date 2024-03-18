package com.book.event.entity.sql.event;

import com.book.event.entity.sql.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventType extends AuditableEntity {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native"
    )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventType")
    private Set<Event> events;
}
