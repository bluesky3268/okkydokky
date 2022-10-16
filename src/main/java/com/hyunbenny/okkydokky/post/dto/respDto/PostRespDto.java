package com.hyunbenny.okkydokky.post.dto.respDto;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRespDto {

    private Long postNo;

    private BoardType boardType;

    private String title;

    private String cont;

    private String passwd;

    private int likes;

    private long views;

    private String userId;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    public PostRespDto toPostRespDto(Post post) {
        this.postNo = post.getPostNo();
        this.boardType = post.getBoardType();
        this.title = post.getTitle();
        this.cont = post.getCont();
        this.passwd = post.getPasswd();
        this.likes = post.getLikes();
        this.views = post.getViews();
        this.userId = post.getUser().getUserId();
        this.regDate = post.getRegDate();
        this.updDate = post.getUpdDate();
        return this;
    }

    @Override
    public String toString() {
        return "PostRespDto{" +
                "postNo=" + postNo +
                ", postType=" + boardType +
                ", title='" + title + '\'' +
                ", cont='" + cont + '\'' +
                ", passwd='" + passwd + '\'' +
                ", likes=" + likes +
                ", views=" + views +
                ", userId='" + userId + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
