package com.yurakim.readingtrace.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

//TODO: create OAuthAccount table
//TODO: consider separating USER and AUTH entities (a dedicated auth related table)

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles;

    @JsonIgnore
    private LocalDateTime lastLoginAt;

    @JsonIgnore
    private LocalDateTime lastFailedLoginAt;

    @Column(nullable = false)
    @Min(0)
    @JsonIgnore
    private Integer failedLoginAttempts = 0;

    @JsonIgnore
    private String failedLoginReason;

    @Column(nullable = false)
    @JsonIgnore
    private Boolean isAccountLocked = false;

    @JsonIgnore
    private LocalDateTime accountLockedAt;

    @JsonIgnore
    private LocalDateTime accountUnlockedAt;

    @Column(name="o_auth2_login", nullable = false)
    @JsonIgnore
    private Boolean oAuth2Login = false;

    @Column(name="o_auth2_provider")
    @JsonIgnore
    private String oAuth2Provider;

}
