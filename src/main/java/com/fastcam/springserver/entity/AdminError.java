package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class AdminError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int errornum;
    private LocalDateTime time;
    private String type;
    private String level;
    private String method;
    @Column(columnDefinition = "varchar(1000)")
    private String api;
    @Column(columnDefinition = "TEXT")
    private String msg;
    private int statusCode;
    @Column(columnDefinition = "varchar(1) default 'N'")
    private String state;
}
