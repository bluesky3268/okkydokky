package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.post.dto.PostSaveReqDto;
import com.hyunbenny.okkydokky.post.dto.respDto.PostRespDto;
import com.hyunbenny.okkydokky.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        Users user = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("user")
                .email("user1@email.com")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    public void cleanUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 등록 테스트")
    public void savePost() throws Exception {
        // given
        PostSaveReqDto saveReqDto = PostSaveReqDto.builder()
                .postType(PostType.C)
                .title("게시글 제목")
                .cont("게시글 내용")
                .passwd("1234")
                .userId("user1")
                .build();

        // when
        postService.savePost(saveReqDto);

        // then
        Post findPost = postRepository.findById(1L).get();

        assertThat(findPost.getPostType()).isEqualTo(PostType.C);
        assertThat(findPost.getTitle()).isEqualTo("게시글 제목");
        assertThat(findPost.getCont()).isEqualTo("게시글 내용");
        assertThat(findPost.getUser().getUserId()).isEqualTo("user1");

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 등록 - 잘못된 회원ID정보")
    public void savePostWithIllegalUserId() throws Exception {
        // given
        String userId = "user10";
        PostSaveReqDto saveReqDto = PostSaveReqDto.builder()
                .postType(PostType.C)
                .title("게시글 제목")
                .cont("게시글 내용")
                .passwd("1234")
                .userId(userId)
                .build();

        // expected
        assertThrows(IllegalArgumentException.class, () -> {
            postService.savePost(saveReqDto);
        });

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.savePost(saveReqDto);
        }, "존재하지 않는 유저정보입니다. userId : " + userId);

        assertThat("존재하지 않는 유저정보입니다. userId : " + userId).isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 단건 조회")
    public void getPostById() throws Exception {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        Post savedPost = postRepository.save(post);

        log.info("========== savedPost : {} ==========", savedPost);

        // when
        Long postNo = 1L;
        PostRespDto postResp = postService.getPost(postNo);

        // then
        assertThat(postResp.getPostNo()).isEqualTo(postNo);
        assertThat(postResp.getPostType()).isEqualTo(post.getPostType());
        assertThat(postResp.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResp.getCont()).isEqualTo(post.getCont());
        assertThat(postResp.getUserId()).isEqualTo(post.getUser().getUserId());
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 단건 조회 실패 - 게시글 없음")
    public void getPostById_fail() throws Exception {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // expected다
        Long postNo = 10L;
        assertThrows(PostNotFoundException.class, () -> {
            postService.getPost(postNo);
        });

        Throwable exception = assertThrows(PostNotFoundException.class, () -> {
            postService.getPost(postNo);
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

}
