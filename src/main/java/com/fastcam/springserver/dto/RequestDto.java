package com.fastcam.springserver.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class RequestDto {
    private String session_id;
    private String character;
    private String message;
    private List<MessageItem> history;

    @Data
    public static class MessageItem {
        private String sender;
        private String content;
    }
}