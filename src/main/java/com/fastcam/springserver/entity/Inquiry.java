package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquirynum;

    @Column(nullable = false)
    private int userid;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false,
            columnDefinition = "varchar(20) default '대기중'")
    private String status = "대기중";

    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
}