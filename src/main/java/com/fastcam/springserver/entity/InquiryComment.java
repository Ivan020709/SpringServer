package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// 문의사항에 작성되는 댓글을 저장하는 테이블입니다.
@Entity
@Data
public class InquiryComment {

    // 댓글 번호는 DB에서 자동으로 증가합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 어떤 문의글에 작성된 댓글인지 저장합니다.
    @Column(name = "inquiry_id", nullable = false)
    private int inquiryId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
