package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.common.util.Pager;
import com.hyunbenny.okkydokky.dto.comment.respDto.CommentListRespDto;
import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.repository.comments.CommentsRepository;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class CommentsServiceTest {

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

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

        List<Comments> comments = IntStream.range(1, 50).mapToObj(i -> Comments.builder()
                .comment("댓글 테스트" + i)
                .post(post)
                .regUser(user)
                .regDate(LocalDateTime.now())
                .build())
                .collect(Collectors.toList());

        commentsRepository.saveAll(comments);
    }

    @AfterEach
    public void cleanUp() {
        commentsRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 등록")
    public void saveComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("대댓글 등록")
    public void saveChildComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 목록 조회 - 페이징")
    public void getCommentList() {
        // given
        Long postNo = 1L;
        int page = 1;
        int pageSize = 20;
        Pager pager = new Pager(page, pageSize);
        pager.setDirection(Sort.Direction.DESC);

        Post findPost = postRepository.findById(postNo).get();
        Users findUser = userRepository.findByUserId("user1").get();
        Comments findComment = commentsRepository.findById(10L).get();

        // 대댓글 저장
        List<Comments> comments = IntStream.range(1, 11).mapToObj(i -> Comments.builder()
                .comment("=== 대댓글 테스트" + i)
                .post(findPost)
                .regUser(findUser)
                .parentComm(findComment)
                .regDate(LocalDateTime.now())
                .build())
                .collect(Collectors.toList());
        commentsRepository.saveAll(comments);

        // when
        List<CommentListRespDto> commentList = commentsService.getCommentList(postNo, pager);

        // then
        assertThat(commentList.size()).isEqualTo(20);
        assertThat(commentList.get(9).getChild().size()).isEqualTo(10);

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 목록 조회 실패 - 게시글 없음")
    public void getCommentList_Fail_NotFoundPost() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 목록 조회 실패 - 댓글 없음")
    public void getCommentList_Fail_NotFoundComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 목록 조회 실패 - 유저 없음")
    public void getCommentList_Fail_NotFoundUser() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 수정")
    public void modifyComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 수정 실패 - 게시글 없음")
    public void modifyComment_fail_NotFoundPost() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 수정 실패 - 댓글 없음")
    public void modifyComment_fail_NotFoundComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 수정 실패 - 유저 없음")
    public void modifyComment_fail_NotFoundUser() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 삭제")
    public void deleteComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 삭제 실패 - 게시글 없음")
    public void deleteComment_fail_NotFoundPost() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 없음")
    public void deleteComment_fail_NotFoundComment() {

    }

    @Sql("classpath:testdb/commentsTableReset.sql")
    @Test
    @DisplayName("댓글 삭제 실패 - 유저 없음")
    public void deleteComment_fail_NotFoundUser() {

    }


}
