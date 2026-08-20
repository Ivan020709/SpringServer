package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChatList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String session_id;

    @Column(nullable = false)
    private String user_id;

    private String title;
}
