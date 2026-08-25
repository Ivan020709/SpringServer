package com.fastcam.springserver.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ResponseDto {
    private String session_id;
    private String character;
    private String message;
    private String summary;
    private Emotion emotion;
    private String diary;

    // AI가 만든 감정일기를 DB에 저장하면 emotion_diary 테이블에 id가 생깁니다.
    // Feel.js의 공유하기 버튼이 방금 저장한 일기를 찾을 수 있도록
    // 저장된 감정일기 번호를 이 필드에 담아 프론트로 전달합니다.
    private Integer diaryId;

    @Data
    public static class Emotion {
        private String main_emotion;
        private List<String> emotions;
        private int intensity;
        private String emoji;
    }
}
