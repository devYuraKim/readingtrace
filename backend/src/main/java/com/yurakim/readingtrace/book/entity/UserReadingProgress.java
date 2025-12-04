package com.yurakim.readingtrace.book.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString(callSuper = true)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserReadingProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long userReadingStatusId;

    @Column(nullable = false)
    private Integer currentPage;

}
