package com.fastcam.springserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestDto {
    private int userid;
    private String session_id;
    private String character;
    private String message;
    // Spring 서버가 회원의 친밀도를 조회하여 FastAPI에 전달합니다.
    private int affinityLevel;
    private String affinityName;
    private String toneGuide;
    private List<MessageItem> history;

    @Data
    public static class MessageItem {
        private String sender;
        private String content;
    }
}
