package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardReportRepository extends JpaRepository<BoardReport, Integer> {
    Optional<BoardReport> findByBoardIdAndReporterId(int boardId, int reporterId);
    void deleteAllByBoardId(int boardId);
}
