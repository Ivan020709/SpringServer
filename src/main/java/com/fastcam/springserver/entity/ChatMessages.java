package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private String Sender;
    @Column(columnDefinition = "varchar(2000)", nullable = false)
    private String content;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
}
