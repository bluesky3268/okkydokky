package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import com.hyunbenny.okkydokky.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        Users user = Users.builder()
                .userNo(1L)
                .userId("user1")
                .passwd("1234")
                .nickname("user")
                .email("user1@email.com")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("게시글 등록")
    public void savePost() {
        // given
        Users user = userRepository.findById(1L).get();

        Post post = Post.builder()
                .postNo(1L)
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getPostType()).isEqualTo(PostType.C);
        assertThat(savedPost.getTitle()).isEqualTo("title1");
        assertThat(savedPost.getCont()).isEqualTo("cont1");
        assertThat(savedPost.getPasswd()).isEqualTo("1234");
        assertThat(savedPost.getUser().getUserId()).isEqualTo("user1");
    }

    @Test
    @DisplayName("게시글 단건 ID로 조회")
    public void getPostById() {
        // given
        Users user = userRepository.findById(1L).get();

        Post post = Post.builder()
                .postNo(1L)
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);


        // when
        Post findPost = postRepository.findById(1L).get();

        // then
        assertThat(findPost.getPostType()).isEqualTo(savedPost.getPostType());
        assertThat(findPost.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(findPost.getCont()).isEqualTo(savedPost.getCont());
        assertThat(findPost.getPasswd()).isEqualTo(savedPost.getPasswd());
        assertThat(findPost.getUser()).isEqualTo(savedPost.getUser());
        assertThat(findPost.getRegDate()).isEqualTo(savedPost.getRegDate());

    }

    @Test
    @DisplayName("게시글 단건 TITLE로 조회")
    public void getPostByTitle() {
        // given
        Users user = userRepository.findById(1L).get();

        Post post = Post.builder()
                .postNo(1L)
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        // when
        Post findPost = postRepository.findByTitle("title1");

        // then
        assertThat(findPost.getPostType()).isEqualTo(savedPost.getPostType());
        assertThat(findPost.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(findPost.getCont()).isEqualTo(savedPost.getCont());
        assertThat(findPost.getPasswd()).isEqualTo(savedPost.getPasswd());
        assertThat(findPost.getUser()).isEqualTo(savedPost.getUser());
        assertThat(findPost.getRegDate()).isEqualTo(savedPost.getRegDate());

    }


}
