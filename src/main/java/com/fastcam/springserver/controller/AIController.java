package com.fastcam.springserver.controller;

import com.fastcam.springserver.dto.RequestDto;
import com.fastcam.springserver.dto.ResponseDto;
import com.fastcam.springserver.entity.EmotionDiary;
import com.fastcam.springserver.service.AIService;
import com.fastcam.springserver.service.AffinityService;
import com.fastcam.springserver.service.EmotionDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AffinityService affinityService;

    public AIController(AIService aiService, AffinityService affinityService) {
        this.aiService = aiService;
        this.affinityService = affinityService;
    }

    @Autowired
    EmotionDiaryService eds;


    // =====================================================
    // AI와 대화
    // =====================================================

    @PostMapping("/chat")
    public ResponseEntity<ResponseDto> chat(
            @RequestBody RequestDto req
    ) {

        // 로그인 회원의 현재 친밀도에 맞는 말투 안내를 FastAPI에 같이 전달합니다.
        if (req.getUserid() > 0) {
            java.util.Map<String, Object> info = affinityService.myInfo(req.getUserid());
            req.setAffinityLevel((Integer) info.get("level"));
            req.setAffinityName((String) info.get("levelName"));
            req.setToneGuide(affinityService.toneGuide(req.getUserid()));
        }

        System.out.println("=================================");
        System.out.println("AI 채팅 요청");
        System.out.println("세션 ID : " + req.getSession_id());
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
        System.out.println("사용자 ID : " + req.getUserid());
        System.out.println("세션 ID : " + req.getSession_id());
        System.out.println("캐릭터 : " + req.getCharacter());
        System.out.println(
                "대화 수 : " +
                        (req.getHistory() == null
                                ? 0
                                : req.getHistory().size())
        );
        System.out.println("=================================");


        // 1. AI 분석
        ResponseDto response =
                aiService.analyze(req);


        // 2. AI 분석 결과를 감정일기로 저장합니다.
        // 저장된 객체를 받아야 새로 만들어진 일기 번호를 확인할 수 있습니다.
        EmotionDiary savedDiary =
                eds.saveFromAiResult(
                        req.getUserid(),
                        response
                );

        // 3. Feel.js가 공유 요청에 사용할 수 있도록 일기 번호를 응답에 넣습니다.
        response.setDiaryId(savedDiary.getId());

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
