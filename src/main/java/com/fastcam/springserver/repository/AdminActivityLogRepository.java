package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.AdminActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActivityLogRepository extends JpaRepository<AdminActivityLog, Integer> {
}
