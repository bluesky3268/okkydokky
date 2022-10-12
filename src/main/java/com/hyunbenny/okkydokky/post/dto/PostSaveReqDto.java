package com.hyunbenny.okkydokky.post.dto;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSaveReqDto {

    private PostType postType;

    private String title;

    private String cont;

    private String passwd;

    private String userId;

    @Builder
    public PostSaveReqDto(PostType postType, String title, String cont, String passwd, String userId) {
        this.postType = postType;
        this.title = title;
        this.cont = cont;
        this.passwd = passwd;
        this.userId = userId;
    }

    public Post toEntity(Users user) {
        return Post.builder()
                .postType(this.postType)
                .title(this.title)
                .cont(this.cont)
                .passwd(this.passwd)
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
    }
}
