package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import com.hyunbenny.okkydokky.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 등록")
    public void savePost() {
        // given
        Users user = userRepository.findByUserId("user1").get();

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

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 단건 ID로 조회")
    public void getPostById() {
        // given
        Users user = userRepository.findByUserId("user1").get();

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

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 단건 TITLE로 조회")
    public void getPostByTitle() {
        // given
        Users user = userRepository.findByUserId("user1").get();

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

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 제목 수정")
    public void updatePostTitle() {
        // given
        Users user = userRepository.findByUserId("user1").get();
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
        String updateTitle = "title1_update!!";

        savedPost.updateTitle(updateTitle);

        // then
        Post afterUpdate = postRepository.findById(1L).get();
        assertThat(afterUpdate.getTitle()).isEqualTo(updateTitle);
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 내용 수정")
    public void updatePostCont() {
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
        log.info("========== savedPost : {} ==========", savedPost.toString());

        // when
        String updateCont = "cont1_update!!";

        savedPost.updateCont(updateCont);

        // then
        Post afterUpdate = postRepository.findById(1L).get();
        assertThat(afterUpdate.getCont()).isEqualTo(updateCont);
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("게시글 삭제")
    public void deletePostById() {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(1L)
                .postType(PostType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        postRepository.save(post);

        // when
        Long postId = 1L;
        postRepository.deleteById(postId);

        // then
        Optional<Post> findPost = postRepository.findById(postId);
        assertFalse(findPost.isPresent());
        assertTrue(findPost.isEmpty());
    }

}
