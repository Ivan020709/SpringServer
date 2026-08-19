package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class NoticeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticenum;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    private int viewcount;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
    @Column(columnDefinition = "varchar(1) default 'N'")
    private String fixed;
}
