package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.RequestDto;
import com.fastcam.springserver.dto.ResponseDto;
import com.fastcam.springserver.service.AIService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin({
        "http://localhost:3000",
        "http://localhost:3001"
})
public class AIController {

    private final AIService aiService;


    public AIController(AIService aiService) {

        this.aiService = aiService;
    }


    // =====================================================
    // AI와 대화
    // =====================================================

    @PostMapping("/chat")
    public ResponseEntity<ResponseDto> chat(
            @RequestBody RequestDto req
    ) {

        System.out.println("=================================");
        System.out.println("AI 채팅 요청");
        System.out.println("캐릭터 : " + req.getCharacter());
        System.out.println("메시지 : " + req.getMessage());
        System.out.println("=================================");


        ResponseDto response =
                aiService.chat(req);


        System.out.println("AI 응답 : " + response.getMessage());


        return ResponseEntity.ok(response);
    }


    // =====================================================
    // 대화 종료
    // 요약 → 감정 → 일기
    // =====================================================

    @PostMapping("/analyze")
    public ResponseEntity<ResponseDto> analyze(
            @RequestBody RequestDto req
    ) {

        System.out.println("=================================");
        System.out.println("AI 대화 분석 요청");
        System.out.println("캐릭터 : " + req.getCharacter());
        System.out.println("대화 수 : " +
                (req.getHistory() == null
                        ? 0
                        : req.getHistory().size()));
        System.out.println("=================================");


        ResponseDto response =
                aiService.analyze(req);


        System.out.println("===== 분석 결과 =====");

        System.out.println(
                "요약 : " + response.getSummary()
        );

        if (response.getEmotion() != null) {

            System.out.println(
                    "감정 : " +
                            response.getEmotion().getMain_emotion()
            );

            System.out.println(
                    "이모지 : " +
                            response.getEmotion().getEmoji()
            );
        }

        System.out.println(
                "일기 : " + response.getDiary()
        );


        return ResponseEntity.ok(response);
    }
}