package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(
        name = "daily_mood",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_daily_mood_user_date",
                columnNames = {"user_id", "mood_date"}
        ),
        indexes = @Index(name = "idx_daily_mood_date", columnList = "mood_date")
)
@Data
public class DailyMood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "mood_date", nullable = false)
    private LocalDate moodDate;

    @Column(nullable = false, length = 20)
    private String mood;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
