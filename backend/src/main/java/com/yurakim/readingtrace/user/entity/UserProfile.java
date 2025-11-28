package com.yurakim.readingtrace.user.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true)
    private String nickname;

    private String profileImageUrl;

    private Long readingGoalCount;
    private String readingGoalUnit;
    private String readingGoalTimeframe;

    private String favoredGenres;

    @Column(nullable = false)
    private Boolean isOnboardingCompleted = false;

}
