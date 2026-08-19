package com.fastcam.springserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto {
    private String session_id;
    private String character;
    private String message;
    private String summary;
    private Emotion emotion;
    private String diary;

    @Getter
    @Setter
    public static class Emotion {
        private String main_emotion;
        private List<String> emotions;
        private int intensity;
        private String emoji;
    }
}