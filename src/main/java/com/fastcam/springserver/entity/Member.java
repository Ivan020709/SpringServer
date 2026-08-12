package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;
    private String pwd;
    private String name;
    @Column(nullable = false)
    private String nickname;
    @Column (nullable = false)
    private String email;
    private String phone;
    @Column(columnDefinition = "varchar(8)")
    private String birth;
    @Column (nullable = false , columnDefinition = "varchar(4)")
    private String mbti;
    @Column(length = 500)
    private String savefilename;
    private String zip_num;
    private String address1;
    private String address2;
    private String address3;
    @CreationTimestamp
    @Column(columnDefinition = "datetime default now()")
    private Timestamp indate;
    @Column(columnDefinition = "varchar(10) default 'LOCAL'")
    private String provider;
    private String snsid;
    @Column(columnDefinition = "varchar(1) default 'N'")
    private String editcom;
    @Column(columnDefinition = "default 'USER'")
    private String role;
}
