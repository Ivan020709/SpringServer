package com.fastcam.springserver.dto;

import lombok.Data;

// 프론트에서 보낸 문의 댓글 내용을 받는 객체입니다.
@Data
public class InquiryCommentRequest {
    private String content;
}
