package com.yurakim.readingtrace.auth.entity.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    private String tokenId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean isExpired = false;

}
