package com.hyunbenny.okkydokky.post.dto.respDto;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import com.hyunbenny.okkydokky.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRespDto {

    private Long postNo;

    private PostType postType;

    private String title;

    private String cont;

    private String passwd;

    private int recommend;

    private long views;

    private String userId;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    public PostRespDto toPostRespDto(Post post) {
        this.postNo = post.getPostNo();
        this.postType = post.getPostType();
        this.title = post.getTitle();
        this.cont = post.getCont();
        this.passwd = post.getPasswd();
        this.recommend = post.getRecommend();
        this.views = post.getViews();
        this.userId = post.getUser().getUserId();
        this.regDate = post.getRegDate();
        this.updDate = post.getUpdDate();
        return this;
    }


}
