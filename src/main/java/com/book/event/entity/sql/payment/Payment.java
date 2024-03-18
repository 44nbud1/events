package com.book.event.entity.sql.payment;

import com.book.event.entity.sql.AuditableEntity;
import com.book.event.entity.sql.book.Book;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "payments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends AuditableEntity {

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

    @Column(name = "method", nullable = false)
    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
