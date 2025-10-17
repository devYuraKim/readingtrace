package com.yurakim.readingtrace.book.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "bookId"}))
@Entity
public class UserBookStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long shelfId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String visibility;

    private Integer rating;

    private Date startDate;
    
    private Date endDate;
}
