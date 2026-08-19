package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Integer> {
    Optional<BoardLike> findByBoardIdAndUserId(int boardId, int userId);
    long countByBoardId(int boardId);
    void deleteAllByBoardId(int boardId);
}
