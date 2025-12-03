package com.yurakim.readingtrace.book.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
@Getter @NoArgsConstructor @AllArgsConstructor
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
    private Long currentPage;

}
