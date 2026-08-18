package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "board_report", uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "reporter_id"}))
@Data
public class BoardReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "board_id", nullable = false)
    private int boardId;
    @Column(name = "reporter_id", nullable = false)
    private int reporterId;
    @Column(nullable = false, length = 50)
    private String reason;
    @Column(length = 500)
    private String detail;
    @Column(nullable = false, length = 20)
    private String status = "PENDING";
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
