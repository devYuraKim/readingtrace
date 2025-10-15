package com.yurakim.readingtrace.shelf.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "name"}),
                @UniqueConstraint(columnNames = {"userId", "slug"})
        }
)
@Entity
public class Shelf extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    private String description;

    @Column(nullable = false,  columnDefinition = "INT DEFAULT 0")
    private Integer bookCount = 0;

}
