package com.book.event.entity.sql.address;

import com.book.event.entity.sql.AuditableEntity;
import com.book.event.entity.sql.event.Event;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "locations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location extends AuditableEntity {

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

    @Column(name = "venue", nullable = false, length = 64)
    private String venue;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private Set<Event> events;
}
