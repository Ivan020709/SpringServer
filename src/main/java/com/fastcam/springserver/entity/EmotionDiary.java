package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "emotion_diary", indexes = {
        @Index(name = "idx_emotion_diary_shared_date", columnList = "is_shared, diary_date"),
        @Index(name = "idx_emotion_diary_user_date", columnList = "user_id, diary_date")
})
@Data
public class EmotionDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "diary_date", nullable = false)
    private Date diaryDate;

    @Column(nullable = false, length = 30)
    private String mood;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "is_shared", nullable = false)
    private boolean shared;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String emoji;
}
