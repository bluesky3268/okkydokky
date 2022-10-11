package com.hyunbenny.okkydokky.repository;

import com.hyunbenny.okkydokky.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUserId(String userId);

    void deleteByUserNo(Long userNo);
    void deleteByUserId(String userId);
}
