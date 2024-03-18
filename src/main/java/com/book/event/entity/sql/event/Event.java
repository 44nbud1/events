package com.book.event.entity.sql.event;

import com.book.event.entity.sql.address.Location;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "events")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

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

    @Column(name = "`key`", nullable = false, unique = true)
    private String key;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "promotor", nullable = false, length = 64)
    private String promotor;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private EventType eventType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private Set<EventGroup> eventGroupSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private Set<EventSchedule> eventSchedules;
}
