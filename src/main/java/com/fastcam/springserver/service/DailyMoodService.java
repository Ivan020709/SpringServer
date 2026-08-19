package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.DailyMood;
import com.fastcam.springserver.repository.DailyMoodRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DailyMoodService {
    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");
    private static final Set<String> ALLOWED_MOODS = Set.of(
            "좋아요", "괜찮아요", "그저 그래요", "우울해요", "힘들어요"
    );

    private final DailyMoodRepository repository;

    public DailyMoodService(DailyMoodRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getToday(int userId) {
        Optional<DailyMood> dailyMood = repository.findByUserIdAndMoodDate(userId, today());
        return Map.of(
                "selected", dailyMood.isPresent(),
                "mood", dailyMood.map(DailyMood::getMood).orElse("")
        );
    }

    public DailyMood createToday(int userId, String mood) {
        if (mood == null || !ALLOWED_MOODS.contains(mood)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 기분을 선택해주세요.");
        }

        LocalDate today = today();
        if (repository.findByUserIdAndMoodDate(userId, today).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "오늘의 기분은 이미 기록했습니다.");
        }

        DailyMood dailyMood = new DailyMood();
        dailyMood.setUserId(userId);
        dailyMood.setMoodDate(today);
        dailyMood.setMood(mood);

        try {
            return repository.saveAndFlush(dailyMood);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "오늘의 기분은 이미 기록했습니다.");
        }
    }

    private LocalDate today() {
        return LocalDate.now(SERVICE_ZONE);
    }
}
