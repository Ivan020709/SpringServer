package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class AdminReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportnum;
    private int boardnum;
    private String reporter;
    private String criminal;
    private String reasontype;
    @Column(nullable = false, length = 1000)
    private String content;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
    @Column(columnDefinition = "varchar(1) default 'N'")
    private String status;
}
