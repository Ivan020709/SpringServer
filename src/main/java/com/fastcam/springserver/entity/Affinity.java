package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

/** 회원별 AI 친밀도 경험치와 레벨입니다. */
@Entity
@Table(name = "affinity", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@Data
public class Affinity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "affinity_id")
    private Integer affinityId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "affinity_exp", nullable = false)
    private int affinityExp;

    @Column(name = "affinity_level", nullable = false)
    private int affinityLevel = 1;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
