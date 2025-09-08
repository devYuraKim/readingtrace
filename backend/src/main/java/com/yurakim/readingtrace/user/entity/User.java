package com.yurakim.readingtrace.user.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

//TODO: consider separating USER and AUTH entities (a dedicated auth related table)

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private LocalDateTime lastLoginAt;

    private LocalDateTime lastFailedLoginAt;

    @Column(nullable = false)
    @Min(0)
    private Integer failedLoginAttempts = 0;

    private String failedLoginReason;

    @Column(nullable = false)
    private Boolean isAccountLocked = false;

    private LocalDateTime accountLockedAt;

    private LocalDateTime accountUnlockedAt;

}
