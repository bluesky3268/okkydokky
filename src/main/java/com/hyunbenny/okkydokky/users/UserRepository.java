package com.hyunbenny.okkydokky.users;

import com.hyunbenny.okkydokky.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);

    void deleteByUserNo(Long userNo);
    void deleteByUserId(String userId);
}
