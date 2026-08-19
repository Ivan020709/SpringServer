package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.DailyMood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyMoodRepository extends JpaRepository<DailyMood, Integer> {
    Optional<DailyMood> findByUserIdAndMoodDate(int userId, LocalDate moodDate);
}
