package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.common.code.PointPolicy;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.exception.UserNotExistException;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.dto.post.reqDto.PostSaveReqDto;
import com.hyunbenny.okkydokky.dto.post.reqDto.PostEditReqDto;
import com.hyunbenny.okkydokky.dto.post.respDto.PostListRespDto;
import com.hyunbenny.okkydokky.dto.post.respDto.PostRespDto;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import com.hyunbenny.okkydokky.common.util.Pager;
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

    @Autowired
    private LikeInfoRepository likeInfoRepository;

    @BeforeEach
    public void init() {
        Users user1 = Users.builder()
                .userId("user1")
                .passwd("1234")
                .nickname("user1")
                .email("user1@email.com")
                .build();

        userRepository.save(user1);

        Users user2 = Users.builder()
                .userId("user2")
                .passwd("1234")
                .nickname("user2")
                .email("user2@email.com")
                .build();

        userRepository.save(user2);
    }

    @AfterEach
    public void cleanUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        likeInfoRepository.deleteAll();
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ?????????")
    public void savePost() throws Exception {
        // given
        PostSaveReqDto saveReqDto = PostSaveReqDto.builder()
                .boardType(BoardType.C)
                .title("????????? ??????")
                .cont("????????? ??????")
                .passwd("1234")
                .userId("user1")
                .build();

        // when
        postService.savePost(saveReqDto);

        // then
        Post findPost = postRepository.findById(1L).get();

        assertThat(findPost.getBoardType()).isEqualTo(BoardType.C);
        assertThat(findPost.getTitle()).isEqualTo("????????? ??????");
        assertThat(findPost.getCont()).isEqualTo("????????? ??????");
        assertThat(findPost.getUser().getUserId()).isEqualTo("user1");
        assertThat(findPost.getUser().getPoint()).isEqualTo(PointPolicy.ADD_POST);

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? - ????????? ??????ID??????")
    public void savePostWithIllegalUserId() throws Exception {
        // given
        String userId = "user10";
        PostSaveReqDto saveReqDto = PostSaveReqDto.builder()
                .boardType(BoardType.C)
                .title("????????? ??????")
                .cont("????????? ??????")
                .passwd("1234")
                .userId(userId)
                .build();

        // expected
        assertThrows(UserNotExistException.class, () -> {
            postService.savePost(saveReqDto);
        });

        Throwable exception = assertThrows(UserNotExistException.class, () -> {
            postService.savePost(saveReqDto);
        }, "???????????? ?????? ???????????????. userId : " + userId);

        assertThat("???????????? ?????? ???????????????. userId : " + userId).isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ??????")
    public void getPostById() throws Exception {
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

        log.info("========== savedPost : {} ==========", savedPost);

        // when
        Long postNo = 1L;
        PostRespDto postResp = postService.getPost(postNo);

        // then
        assertThat(postResp.getPostNo()).isEqualTo(postNo);
        assertThat(postResp.getBoardType()).isEqualTo(post.getBoardType());
        assertThat(postResp.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResp.getCont()).isEqualTo(post.getCont());
        assertThat(postResp.getViews()).isEqualTo(savedPost.getViews() + 1);
        assertThat(postResp.getUserId()).isEqualTo(post.getUser().getUserId());
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ?????? ?????? - ????????? ??????")
    public void getPostById_fail() throws Exception {
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
        postRepository.save(post);

        // expected
        Long postNo = 10L;
        assertThrows(PostNotFoundException.class, () -> {
            postService.getPost(postNo);
        });

        Throwable exception = assertThrows(PostNotFoundException.class, () -> {
            postService.getPost(postNo);
        }, "???????????? ???????????? ????????????.");

        assertThat("???????????? ???????????? ????????????.").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ????????? ?????? BoardType C")
    public void getPostListBoardTypeC() throws Exception {
        Users user1 = userRepository.findByUserId("user1").get();
        // given
        List<Post> savedList1 = IntStream.range(1, 51)
                .mapToObj(i -> Post.builder()
                        .boardType(BoardType.C)
                        .title("title" + i)
                        .cont("cont" + i)
                        .passwd("1234")
                        .user(user1)
                        .regDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(savedList1);

        // when
        int page = 1;
        int pageSize = 20;
        Pager pager = new Pager(page, pageSize);
        pager.setDirection(Sort.Direction.DESC);
        Page<PostListRespDto> result = postService.getPostList(BoardType.C, pager);

        // then
        assertThat(result.getContent().size()).isEqualTo(20);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title50");
        assertThat(result.getContent().get(0).getUserId()).isEqualTo("user1");
        assertThat(result.getContent().get(14).getTitle()).isEqualTo("title36");
        assertThat(result.getContent().get(14).getUserId()).isEqualTo("user1");
        assertThat(result.getContent().get(19).getTitle()).isEqualTo("title31");
        assertThat(result.getContent().get(19).getUserId()).isEqualTo("user1");
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ????????? ?????? - BoardType N")
    public void getPostListBoardTypeN() throws Exception {
        Users user2 = userRepository.findByUserId("user2").get();
        // given
        List<Post> savedList1 = IntStream.range(1, 101)
                .mapToObj(i -> Post.builder()
                        .boardType(BoardType.N)
                        .title("title" + i)
                        .cont("cont" + i)
                        .passwd("1234")
                        .user(user2)
                        .regDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(savedList1);

        // when
        int page = 3;
        int pageSize = 20;
        Pager pager = new Pager(page, pageSize);
        pager.setDirection(Sort.Direction.DESC);
        Page<PostListRespDto> result = postService.getPostList(BoardType.N, pager);

        // then
        assertThat(result.getContent().size()).isEqualTo(20);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title60");
        assertThat(result.getContent().get(0).getUserId()).isEqualTo("user2");
        assertThat(result.getContent().get(14).getTitle()).isEqualTo("title46");
        assertThat(result.getContent().get(14).getUserId()).isEqualTo("user2");
        assertThat(result.getContent().get(19).getTitle()).isEqualTo("title41");
        assertThat(result.getContent().get(19).getUserId()).isEqualTo("user2");
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ??????")
    public void updatePostSuccess() {
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

        String updateTitle = "update title1";
        String updateContent = "update cont1";

        PostEditReqDto updateDto = PostEditReqDto.builder()
                .postNo(1L)
                .boardType(BoardType.C)
                .title(updateTitle)
                .cont(updateContent)
                .build();

        // when
        PostRespDto updatedPost = postService.modifyPost(updateDto);

        // then
        assertThat(updatedPost.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedPost.getCont()).isEqualTo(updateContent);
        assertThat(updatedPost.getUpdDate()).isNotNull();
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ?????? - ???????????? ?????? ?????????")
    public void updatePostFail() {
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

        String updateTitle = "update title1";
        String updateContent = "update cont1";

        PostEditReqDto updateDto = PostEditReqDto.builder()
                .postNo(2L)
                .boardType(BoardType.C)
                .title(updateTitle)
                .cont(updateContent)
                .build();

       // expected
        assertThrows(PostNotFoundException.class, () ->
                postService.modifyPost(updateDto));

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ??????")
    public void deletePostSuccess() {
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

        Long deletePostNo = 1L;

        // when
        postService.deletePost(deletePostNo);

        // then
        Optional<Post> findPost = postRepository.findById(deletePostNo);
        assertFalse(findPost.isPresent());
        assertTrue(findPost.isEmpty());
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("????????? ?????? ?????? - ????????? ??????")
    public void deletePostFail() {
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


        Long deletePostNo = 2L;

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            postService.deletePost(deletePostNo);
        });

        Throwable exception = assertThrows(PostNotFoundException.class, () -> {
            postService.deletePost(deletePostNo);
        }, "???????????? ???????????? ????????????.");

        assertThat("???????????? ???????????? ????????????.").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("???????????? ?????? ??????????????? ?????????")
    public void moveBoardSuccess() {
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
        Long postNo = 1L;
        PostRespDto postRespDto = postService.movePostToOtherBoard(postNo, BoardType.Q);

        // then
        assertThat(postRespDto.getPostNo()).isEqualTo(post.getPostNo());
        assertThat(postRespDto.getBoardType()).isEqualTo(BoardType.Q);

    }

    @Sql("classpath:testdb/postTableReset.sql")
    @Test
    @DisplayName("???????????? ?????? ??????????????? ????????? ?????? - ????????? ??????")
    public void moveBoardFail() {
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

        Long postNo = 2L;
        BoardType moveToBoardType = BoardType.Q;

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            postService.movePostToOtherBoard(postNo, moveToBoardType);
        });

        Throwable exception = assertThrows(PostNotFoundException.class, () -> {
            postService.movePostToOtherBoard(postNo, moveToBoardType);
        }, "???????????? ???????????? ????????????.");

        assertThat("???????????? ???????????? ????????????.").isEqualTo(exception.getMessage());

    }
}
