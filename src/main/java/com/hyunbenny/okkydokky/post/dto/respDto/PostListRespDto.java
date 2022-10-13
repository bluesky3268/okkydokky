package com.hyunbenny.okkydokky.post.dto.respDto;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListRespDto {

    private Long postNo;

    private BoardType boardType;

    private String title;

    private int recommend;

    private long views;

    private String userId;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    public PostListRespDto toListRespDto(Post post) {
        this.postNo = post.getPostNo();
        this.boardType = post.getBoardType();
        this.title = post.getTitle();
        this.recommend = post.getRecommend();
        this.views = post.getViews();
        this.userId = post.getUser().getUserId();
        this.regDate = post.getRegDate();
        this.updDate = post.getUpdDate();

        return this;
    }

    @Override
    public String toString() {
        return "PostListRespDto{" +
                "postNo=" + postNo +
                ", boardType=" + boardType +
                ", title='" + title + '\'' +
                ", recommend=" + recommend +
                ", views=" + views +
                ", userId='" + userId + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
