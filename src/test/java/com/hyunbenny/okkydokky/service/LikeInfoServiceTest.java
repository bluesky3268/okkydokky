package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.common.code.LikeStatus;
import com.hyunbenny.okkydokky.common.code.PointPolicy;
import com.hyunbenny.okkydokky.entity.LikeInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.exception.PostNotFoundException;
import com.hyunbenny.okkydokky.exception.UserNotExistException;
import com.hyunbenny.okkydokky.repository.likeInfo.LikeInfoRepository;
import com.hyunbenny.okkydokky.repository.post.PostRepository;
import com.hyunbenny.okkydokky.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class LikeInfoServiceTest {

    @Autowired
    private LikeInfoService likeInfoService;

    @Autowired
    private LikeInfoRepository likeInfoRepository;

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


    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요")
    public void hitTheLikeBtn_increaseLike() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // when
        likeInfoService.hitLikeBtn(postNo, "user2");

        // then
        Post findPost = postRepository.findById(postNo).get();

        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getLikes()).isEqualTo(1);
        assertThat(findPost.getUser().getPoint()).isEqualTo(PointPolicy.GET_LIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 실패 - 게시글 없음")
    public void hitTheLikeBtn_NotFoundPost() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 실패 - 유저 없음")
    public void hitTheLikeBtn_NotFoundUser() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 취소")
    public void cancelLikeBtn() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();

        // when
        likeInfoService.hitLikeBtn(postNo, "user2");
        likeInfoService.hitLikeBtn(postNo, "user2");

        // then
        Users findUser = userRepository.findByUserId("user2").get();
        Post findPost = postRepository.findById(postNo).get();
        LikeInfo findLikeInfo = likeInfoService.findByPostNoAndUserId(postNo, findUser.getUserId());

        assertThat(findUser.getPoint()).isEqualTo(0);
        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getLikes()).isEqualTo(0);
        assertThat(findLikeInfo).isNull();
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 취소 실패 - 게시글 없음")
    public void cancelLikeBtn_NotFoundPost() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
        likeInfoService.hitLikeBtn(postNo, "user2");

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 취소 실패 - 유저 없음")
    public void cancelLikeBtn_NotFoundUser() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
        likeInfoService.hitLikeBtn(postNo, "user2");

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요")
    public void hitTheDislikeBtn() {
        // given
        Long postNo = 1L;
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // when
        likeInfoService.hitDislikeBtn(postNo, "user2");

        // then
        Post findPost = postRepository.findById(postNo).get();

        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getDislikes()).isEqualTo(1);
        assertThat(findPost.getUser().getPoint()).isEqualTo(-PointPolicy.GET_DISLIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 실패 - 게시글 없음")
    public void hitTheDislikeBtn_NotFoundPost() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 실패 - 유저 없음")
    public void hitTheDislikeBtn_NotFoundUser() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 취소")
    public void cancelDislikeBtn() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();

        // when
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());

        // then
        Users findUser = userRepository.findByUserId("user2").get();
        Post findPost = postRepository.findById(postNo).get();
        LikeInfo findLikeInfo = likeInfoService.findByPostNoAndUserId(postNo, findUser.getUserId());

        assertThat(findUser.getPoint()).isEqualTo(0);
        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getDislikes()).isEqualTo(0);
        assertThat(findLikeInfo).isNull();
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 취소 실패 - 게시글 없음")
    public void cancelDislikeBtn_NotFoundPost() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
        likeInfoService.hitDislikeBtn(postNo, "user2");

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());

    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 취소 실패 - 유저 없음")
    public void cancelDislikeBtn_NotFoundUser() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
        likeInfoService.hitDislikeBtn(postNo, "user2");

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 -> 좋아요")
    public void dislikeToLike() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();

        // when
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());
        likeInfoService.hitLikeBtn(postNo, user2.getUserId());

        // then
        Users findUser = userRepository.findByUserId("user2").get();
        Post findPost = postRepository.findById(postNo).get();
        LikeInfo findLikeInfo = likeInfoService.findByPostNoAndUserId(postNo, findUser.getUserId());

        assertThat(findUser.getPoint()).isEqualTo(0);
        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getLikes()).isEqualTo(1);
        assertThat(findPost.getDislikes()).isEqualTo(0);
        assertThat(findPost.getUser().getPoint()).isEqualTo(PointPolicy.GET_LIKE);
        assertThat(findLikeInfo.getStatus()).isEqualTo(LikeStatus.LIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 -> 좋아요 실패 - 게시글 없음")
    public void dislikeToLike_PostNotFound() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitLikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("싫어요 -> 좋아요 실패 - 유저 없음")
    public void dislikeToLike_UserNotFound() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitLikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());
    }


    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 -> 싫어요")
    public void likeToDislike() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();

        // when
        likeInfoService.hitLikeBtn(postNo, user2.getUserId());
        likeInfoService.hitDislikeBtn(postNo, user2.getUserId());

        // then
        Users findUser = userRepository.findByUserId("user2").get();
        Post findPost = postRepository.findById(postNo).get();
        LikeInfo findLikeInfo = likeInfoService.findByPostNoAndUserId(postNo, findUser.getUserId());

        assertThat(findUser.getPoint()).isEqualTo(0);
        assertThat(findPost.getPostNo()).isEqualTo(postNo);
        assertThat(findPost.getLikes()).isEqualTo(0);
        assertThat(findPost.getDislikes()).isEqualTo(1);
        assertThat(findPost.getUser().getPoint()).isEqualTo(-PointPolicy.GET_DISLIKE);
        assertThat(findLikeInfo.getStatus()).isEqualTo(LikeStatus.DISLIKE);
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 -> 싫어요 실패 - 게시글 없음")
    public void likeToDislike_PostNotFound() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();
        likeInfoService.hitLikeBtn(postNo, user2.getUserId());

        // expected
        assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        });

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo + 1, "user2");
        }, "게시글이 존재하지 않습니다.");

        assertThat("게시글이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Sql("classpath:testdb/likeInfoTableReset.sql")
    @Test
    @DisplayName("좋아요 -> 싫어요 실패 - 유저 없음")
    public void likeToDislike_UserNotFound() {
        Long postNo = 1L;
        // given
        Users user = userRepository.findByUserId("user1").get();
        Post post = Post.builder()
                .postNo(postNo)
                .boardType(BoardType.C)
                .title("title1")
                .cont("cont1")
                .passwd("1234")
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        Users user2 = userRepository.findByUserId("user2").get();
        likeInfoService.hitLikeBtn(postNo, user2.getUserId());

        // expected
        assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        });

        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            likeInfoService.hitDislikeBtn(postNo, "user3");
        }, "존재하지 않는 유저입니다. userId : user3");

        assertThat("존재하지 않는 유저입니다. userId : user3").isEqualTo(exception.getMessage());
    }

}
