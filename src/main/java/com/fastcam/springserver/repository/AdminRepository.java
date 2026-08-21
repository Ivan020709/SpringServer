package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.AdminReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminReport, Integer> {
    AdminReport findByReportnum(int reportnum);
}
