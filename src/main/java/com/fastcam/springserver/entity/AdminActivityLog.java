package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "admin_activity_log")
public class AdminActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String adminid;
    @Column(nullable = false)
    private String adminname;
    @Column(nullable = false)
    private String activity;
    @Column(nullable = false)
    private String target;
    @Column(nullable = false)
    private String method;
    @Column(nullable = false)
    private String api;
    @Column(nullable = false)
    private String result;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
}
