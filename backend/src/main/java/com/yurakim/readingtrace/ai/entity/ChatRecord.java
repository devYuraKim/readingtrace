package com.yurakim.readingtrace.ai.entity;

import com.yurakim.readingtrace.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
public class ChatRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;

    private Date timestamp;
    private String model;
    @Column(columnDefinition = "LONGTEXT")
    private String userMessage;
    @Column(columnDefinition = "LONGTEXT")
    private String assistantMessage;

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    private String finishReason;
    private Boolean isSuccess;
    private String error;

}
