package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "board_comment", indexes = {
        @Index(name = "idx_board_comment_board", columnList = "board_id"),
        @Index(name = "idx_board_comment_user", columnList = "user_id")
})
@Data
public class BoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "board_id", nullable = false)
    private int boardId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
