package com.hyunbenny.okkydokky.users;

import com.hyunbenny.okkydokky.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 저장")
    public void saveUser() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();

        // when
        Users savedUser = userRepository.save(user);

        log.info("savedUser : {}", savedUser.toString());

        // then
        assertThat(savedUser.getUserId()).isEqualTo(userIdParam);
        assertThat(savedUser.getPasswd()).isEqualTo(passwdParam);
        assertThat(savedUser.getEmail()).isEqualTo(emailParam);
        assertThat(savedUser.getNickname()).isEqualTo(nicknameParam);
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 아이디로 조회-성공")
    public void getUserByUserId_Success() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();
        userRepository.save(user);

        // when
        Users findUser = userRepository.findByUserId(userIdParam);

        // then
        assertThat(findUser.getUserId()).isEqualTo(userIdParam);
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 아이디로 조회-회원 없음")
    public void getUserByUserId_Fail() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();
        userRepository.save(user);

        // when
        Users findUser = userRepository.findByUserId("userB");

        // then
        assertThat(findUser).isNull();
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 비밀번호 수정")
    public void updateUserPassword() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();


        Users savedUser = userRepository.save(user);

        // when
        String updatePassword = "passwordUpdate";
        savedUser.updatePasswd(updatePassword);

        Users findUser = userRepository.findByUserId(userIdParam);

        // then
        assertThat(findUser.getPasswd()).isEqualTo(updatePassword);
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 닉네임 수정")
    public void updateUserNickname() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();


        Users savedUser = userRepository.save(user);

        // when
        String updateNick = "USER_A_UPDATE";
        savedUser.updateNickname(updateNick);

        Users findUser = userRepository.findByUserId(userIdParam);

        // then
        assertThat(findUser.getNickname()).isEqualTo(updateNick);
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 전화번호 수정")
    public void updateUserTel() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";
        String telParam = "01012345678";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .tel(telParam)
                .build();


        Users savedUser = userRepository.save(user);

        // when
        String updateTel = "01098765432";
        savedUser.updateTel(updateTel);

        Users findUser = userRepository.findByUserId(userIdParam);

        // then
        assertThat(findUser.getTel()).isEqualTo(updateTel);
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 삭제 - userNo로 삭제")
    public void deleteByUserNo() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();
        userRepository.save(user);

        // when
        userRepository.deleteByUserNo(userNoParam);

        // then
        Optional<Users> findUser = userRepository.findById(userNoParam);

        assertFalse(findUser.isPresent());
        assertTrue(findUser.isEmpty());
    }

    @Test
    @Sql("classpath:testdb/userTableReset.sql")
    @DisplayName("유저 삭제 - userId로 삭제")
    public void deleteByUserId() {
        // given
        Long userNoParam = 1L;
        String userIdParam = "userA";
        String passwdParam = "password";
        String emailParam = "userA@abc.com";
        String nicknameParam = "USER_A";

        Users user = Users.builder()
                .userNo(userNoParam)
                .userId(userIdParam)
                .passwd(passwdParam)
                .email(emailParam)
                .nickname(nicknameParam)
                .build();
        userRepository.save(user);

        // when
        userRepository.deleteByUserId(userIdParam);

        // then
        Users findUser = userRepository.findByUserId(userIdParam);

        assertThat(findUser).isNull();
    }
}
