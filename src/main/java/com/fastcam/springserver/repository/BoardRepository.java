package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Integer, Board> {
}
