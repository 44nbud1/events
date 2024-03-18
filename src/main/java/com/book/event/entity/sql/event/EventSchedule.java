package com.book.event.entity.sql.event;

import com.book.event.entity.sql.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "event_schedules")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSchedule extends AuditableEntity {

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

    @Column(name = "`limit`", nullable = false)
    private Integer limit;

    @Column(name = "limit_ratio_type", nullable = false)
    private String limitRatioType;

    @Column(name = "limit_ratio_value", nullable = false)
    private String limitRatioValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
