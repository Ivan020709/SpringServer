package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.RequestDto;
import com.fastcam.springserver.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AIService {

    private final WebClient webClient;


    public AIService() {

        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }


    // =====================================================
    // AI 채팅
    // =====================================================

    public ResponseDto chat(RequestDto req) {

        return webClient.post()
                .uri("/chat")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .block();
    }


    // =====================================================
    // 대화 분석
    // 요약 → 감정 → 일기
    // =====================================================

    public ResponseDto analyze(RequestDto req) {

        return webClient.post()
                .uri("/analyze")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .block();
    }
}