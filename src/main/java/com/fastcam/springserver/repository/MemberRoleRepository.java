package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Integer> {

    MemberRole findByEmail(String email);

}