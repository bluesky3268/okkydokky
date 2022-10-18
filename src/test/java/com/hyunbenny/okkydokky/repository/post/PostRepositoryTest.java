package com.hyunbenny.okkydokky.repository.post;

import com.hyunbenny.okkydokky.common.util.Pager;
import com.hyunbenny.okkydokky.config.QueryDslTestConfig;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Import(QueryDslTestConfig.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getBoardType()).isEqualTo(BoardType.C);
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
                .boardType(BoardType.C)
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
        assertThat(findPost.getBoardType()).isEqualTo(savedPost.getBoardType());
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
                .boardType(BoardType.C)
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
        assertThat(findPost.getBoardType()).isEqualTo(savedPost.getBoardType());
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
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        // when
        String updateTitle = "title1_update!!";

        savedPost.modifyTitle(updateTitle);

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
                .boardType(BoardType.C)
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

        savedPost.modifyCont(updateCont);

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
                .boardType(BoardType.C)
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

    @Test
    @DisplayName("게시글 조회 - 페이징")
    public void findAllPostsWithPaging() {
        Users user = userRepository.findByUserId("user1").get();
        // given
        List<Post> savedList = IntStream.range(1, 51)
                .mapToObj(i -> Post.builder()
                        .boardType(BoardType.C)
                        .title("title" + i)
                        .cont("cont" + i)
                        .passwd("1234")
                        .user(user)
                        .regDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(savedList);

        // when
        int page = 1;
        int pageSize = 20;
        Pager pager = new Pager(page, pageSize);
        Page<Post> posts = postRepository.findAllPostsWithPaging(BoardType.C, pager.of("POST_NO"));

        // then
        assertThat(posts.getContent().size()).isEqualTo(20);
        assertThat(posts.getPageable().getOffset()).isEqualTo(0);
        assertThat(posts.getPageable().getPageSize()).isEqualTo(20);
        assertThat(posts.getPageable().getPageNumber()).isEqualTo(0);

    }

}
