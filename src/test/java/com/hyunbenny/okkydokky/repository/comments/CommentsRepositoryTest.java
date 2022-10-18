package com.hyunbenny.okkydokky.repository.comments;

import com.hyunbenny.okkydokky.config.QueryDslTestConfig;
import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Import(QueryDslTestConfig.class)
@DataJpaTest
class CommentsRepositoryTest {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeInfoRepository likeInfoRepository;


    @BeforeEach
    public void init() {
        Users user = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("USER1")
                .email("user1@okkydokky.com")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .boardType(BoardType.C)
                .passwd("1234")
                .title("title")
                .cont("content")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
    }

    @AfterEach
    public void cleanUp() {
        commentsRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
        likeInfoRepository.deleteAll();
    }


    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 등록")
    public void saveComment() {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = postRepository.findById(1L).get();

        Comments comment = Comments.builder()
                .comment("댓글 테스트")
                .post(post)
                .regUser(user)
                .regDate(LocalDateTime.now())
                .build();

        // when
        Comments savedComments = commentsRepository.save(comment);

        // then
        assertThat(savedComments.getPost().getPostNo()).isEqualTo(comment.getPost().getPostNo());
        assertThat(savedComments.getRegUser().getUserId()).isEqualTo(comment.getRegUser().getUserId());
        assertThat(savedComments.getComment()).isEqualTo("댓글 테스트");
    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("대댓글 등록")
    public void saveChildComment() {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = postRepository.findById(1L).get();

        Comments comment = Comments.builder()
                .comment("댓글 테스트")
                .post(post)
                .regUser(user)
                .regDate(LocalDateTime.now())
                .build();
        Comments parentComment = commentsRepository.save(comment);

        Comments childComment = Comments.builder()
                .parentComm(parentComment)
                .comment("===== 대댓글 테스트 =====")
                .post(post)
                .regUser(user)
                .regDate(LocalDateTime.now())
                .build();

        // when
        Comments savedChildComment = commentsRepository.save(childComment);

        // then
        Comments findChildComment = commentsRepository.findById(savedChildComment.getCommentNo()).get();
        assertThat(findChildComment.getComment()).isEqualTo(childComment.getComment());
        assertThat(findChildComment.getParentComm().getCommentNo()).isEqualTo(parentComment.getCommentNo());
    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 목록 조회")
    public void saveCommentList() {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = postRepository.findById(1L).get();

        List<Comments> comments = IntStream.range(1, 11)
                .mapToObj(i -> Comments.builder()
                        .comment("댓글 테스트" + i)
                        .post(post)
                        .regUser(user)
                        .regDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        commentsRepository.saveAll(comments);

        // when
        List<Comments> findComments = commentsRepository.findAllByPostNo(post.getPostNo());

        // then
        assertThat(findComments.size()).isEqualTo(10);
    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("대댓글 목록 조회")
    public void saveChildCommentList() {
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = postRepository.findById(1L).get();

        List<Comments> comments = IntStream.range(1, 11)
                .mapToObj(i -> Comments.builder()
                        .comment("댓글 테스트" + i)
                        .post(post)
                        .regUser(user)
                        .regDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        commentsRepository.saveAll(comments);

        Comments child = Comments.builder()
                .comment("대댓글 테스트")
                .post(post)
                .parentComm(comments.get(0))
                .regUser(user)
                .regDate(LocalDateTime.now())
                .build();
        commentsRepository.save(child);

        // when
        List<Comments> allChildComments = commentsRepository.findAllChildComments(post.getPostNo());

        // then
        assertThat(allChildComments.size()).isEqualTo(1);
        log.info("========== 대댓글 테스트 : {}", allChildComments.get(0).toString());
    }

    @Test
    @DisplayName("댓글 수정")
    public void editComment() {

    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteComment() {

    }


}