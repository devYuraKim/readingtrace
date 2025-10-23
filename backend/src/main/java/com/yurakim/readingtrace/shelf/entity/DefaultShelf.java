package com.yurakim.readingtrace.shelf.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@ToString(callSuper = true)
public class DefaultShelf extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(unique = true)
    private Integer orderIndex;

}
