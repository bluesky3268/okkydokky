package com.hyunbenny.okkydokky.repository.likeInfo;

import com.hyunbenny.okkydokky.common.code.LikeStatus;
import com.hyunbenny.okkydokky.config.QueryDslTestConfig;
import com.hyunbenny.okkydokky.entity.LikeInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(QueryDslTestConfig.class)
@Slf4j
@DataJpaTest
public class LikeInfoRepositoryTest {

    @Autowired
    private LikeInfoRepository likeInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void cleanUp() {
        likeInfoRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("게시글 좋아요 버튼 클릭 시 정보 저장")
    public void hitLikeBtn() {
        // given
        Users user = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("user1")
                .email("user1@email.com")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .postNo(1L)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);


        LikeInfo likeInfo = LikeInfo.builder()
                .likeInfoNo(1L)
                .postNo(post.getPostNo())
                .userNo(user.getUserNo())
                .status(LikeStatus.LIKE)
                .build();

        // when
        LikeInfo findLikeInfo = likeInfoRepository.save(likeInfo);

        // then

        assertThat(findLikeInfo.getStatus()).isEqualTo(LikeStatus.LIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("게시글 좋아요 정보 조회 - 성공")
    public void getLikeInfo() {
        // given
        Users user = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("user1")
                .email("user1@email.com")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .postNo(1L)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        LikeInfo likeInfo = LikeInfo.builder()
                .likeInfoNo(1L)
                .postNo(post.getPostNo())
                .userNo(user.getUserNo())
                .status(LikeStatus.LIKE)
                .build();
        likeInfoRepository.save(likeInfo);

        // when
        LikeInfo findLikeInfo = likeInfoRepository.findByPostNoAndUserNo(post.getPostNo(), user.getUserNo());

        // then
        assertThat(findLikeInfo.getPostNo()).isEqualTo(post.getPostNo());
        assertThat(findLikeInfo.getUserNo()).isEqualTo(user.getUserNo());
        assertThat(findLikeInfo.getStatus()).isEqualTo(LikeStatus.LIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("게시글 좋아요 정보 삭제")
    public void deleteLikeInfo() {
        // given
        Users user = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("user1")
                .email("user1@email.com")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .postNo(1L)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        LikeInfo likeInfo = LikeInfo.builder()
                .likeInfoNo(1L)
                .postNo(post.getPostNo())
                .userNo(user.getUserNo())
                .status(LikeStatus.LIKE)
                .build();
        likeInfoRepository.save(likeInfo);

        // when
        likeInfoRepository.deleteById(likeInfo.getLikeInfoNo());


        // then
        Optional<LikeInfo> findLikeInfo = likeInfoRepository.findById(likeInfo.getLikeInfoNo());

        assertFalse(findLikeInfo.isPresent());
        assertTrue(findLikeInfo.isEmpty());
    }


}
