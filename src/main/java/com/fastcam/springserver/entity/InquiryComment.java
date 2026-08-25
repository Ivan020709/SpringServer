package com.fastcam.springserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// 문의사항에 작성되는 댓글을 저장하는 테이블입니다.
@Entity
@Table(name = "inquiry_comment", indexes = {
        @Index(name = "idx_inquiry_comment_inquiry", columnList = "inquiry_id"),
        @Index(name = "idx_inquiry_comment_user", columnList = "user_id")
})
@Data
public class InquiryComment {

    // 댓글 번호는 DB에서 자동으로 증가합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 어떤 문의글에 작성된 댓글인지 저장합니다.
    @Column(name = "inquiry_id", nullable = false)
    private int inquiryId;

    // 댓글을 작성한 회원 번호입니다.
    @Column(name = "user_id", nullable = false)
    private int userId;

    // 댓글 내용입니다.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 처음 저장될 때 작성 시각이 자동으로 들어갑니다.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
