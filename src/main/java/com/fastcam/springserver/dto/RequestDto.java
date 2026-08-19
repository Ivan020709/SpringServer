package com.fastcam.springserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDto {
    private String session_id;
    private String character;
    private String message;
    private List<MessageItem> history;

    @Getter
    @Setter
    public static class MessageItem {
        private String sender;
        private String content;
    }
}