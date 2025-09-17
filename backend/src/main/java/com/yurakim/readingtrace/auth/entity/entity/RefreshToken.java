package com.yurakim.readingtrace.auth.entity.entity;

import com.yurakim.readingtrace.auth.enums.InvalidationCause;
import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(
        indexes = @Index(name = "idx_token_hash", columnList = "tokenHash")
)
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean isRevoked = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvalidationCause invalidationCause = InvalidationCause.NONE;

    private Long replacedById;

}
