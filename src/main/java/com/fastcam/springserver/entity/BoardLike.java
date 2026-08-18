package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "board_like", uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "user_id"}))
@Data
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "board_id", nullable = false)
    private int boardId;

    @Column(name = "user_id", nullable = false)
    private int userId;
}
