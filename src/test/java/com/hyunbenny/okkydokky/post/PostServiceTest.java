package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import com.hyunbenny.okkydokky.post.dto.PostSaveReqDto;
import com.hyunbenny.okkydokky.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("게시글 등록 테스트")
    public void savePost() {
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


}
