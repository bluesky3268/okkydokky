package com.hyunbenny.okkydokky.repository;

import com.hyunbenny.okkydokky.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 아이디로 조회 성공")
    public void getUserByUserId_Success() {
        // given
        Long userNo = 1L;
        String userId = "hello";
        userRepository.save(Users.builder()
                .userNo(userNo)
                .userId(userId)
                .build());

        // when
        Users findUser = userRepository.findByUserId(userId);

        // then
        assertThat(findUser.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("유저 아이디로 조회 실패 - 조회값 없음")
    public void getUserByUserId_Fail() {
        // given
        Long userNo = 1L;
        String userId = "hello";
        userRepository.save(Users.builder()
                .userNo(userNo)
                .userId(userId)
                .build());

        // when
        Users findUser = userRepository.findByUserId("helloWorld");

        // then
        assertThat(findUser).isNull();
    }
}
