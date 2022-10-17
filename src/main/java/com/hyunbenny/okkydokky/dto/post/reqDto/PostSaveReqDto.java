package com.hyunbenny.okkydokky.dto.post.reqDto;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSaveReqDto {

    private BoardType boardType;

    private String title;

    private String cont;

    private String passwd;

    private String userId;

    @Builder
    public PostSaveReqDto(BoardType boardType, String title, String cont, String passwd, String userId) {
        this.boardType = boardType;
        this.title = title;
        this.cont = cont;
        this.passwd = passwd;
        this.userId = userId;
    }

    public Post toEntity(Users user) {
        return Post.builder()
                .boardType(this.boardType)
                .title(this.title)
                .cont(this.cont)
                .passwd(this.passwd)
                .user(user)
                .regDate(LocalDateTime.now())
                .build();
    }
}
