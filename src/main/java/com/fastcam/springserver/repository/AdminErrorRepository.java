package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.AdminError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminErrorRepository extends JpaRepository<AdminError, Integer> {
    List<AdminError> findAllByOrderByErrornumDesc();

    AdminError findByErrornum(int errornum);
}
