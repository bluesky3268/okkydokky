package com.hyunbenny.okkydokky.entity;

import com.hyunbenny.okkydokky.common.code.PointPolicy;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.hyunbenny.okkydokky.dto.post.reqDto.PostEditReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_NO")
    private Long postNo;

    @NotNull
    @Column(name = "POST_TYPE")
    private BoardType boardType;

    @NotNull
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Column(name = "PASSWD")
    private String passwd;

    @NotNull
    @Column(name = "CONT")
    private String cont;

    @Column(name = "LIKES")
    private int likes;

    @Column(name = "DISLIKES")
    private int dislikes;

    @Column(name = "VIEWS")
    private long views;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private Users user;

    @NotNull
    @Column(name = "REG_DATE")
    private LocalDateTime regDate;

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;

    @Builder
    public Post(Long postNo, @NotNull BoardType boardType, @NotNull String title, @NotNull String passwd, @NotNull String cont, int likes, int dislikes, long views, @NotNull Users user, @NotNull LocalDateTime regDate, LocalDateTime updDate) {
        this.postNo = postNo;
        this.boardType = boardType;
        this.title = title;
        this.passwd = passwd;
        this.cont = cont;
        this.likes = likes;
        this.dislikes = dislikes;
        this.views = views;
        this.user = user;
        this.regDate = regDate;
        this.updDate = updDate;
    }

    public void modifyPost(PostEditReqDto editReqDto) {
        this.title = editReqDto.getTitle() == null ? title : editReqDto.getTitle();
        this.cont = editReqDto.getCont() == null ? cont : editReqDto.getCont();
        this.updDate = LocalDateTime.now();
    }

    public void modifyTitle(String editTitle) {
        this.title = editTitle;
        this.updDate = LocalDateTime.now();
    }

    public void modifyCont(String editCont) {
        this.cont = editCont;
        this.updDate = LocalDateTime.now();
    }

    public void moveBoard(BoardType boardType) {
        this.boardType = boardType;
    }

    public void increaseViews() {
        this.views += 1;
    }

    public void increaseLike() {
        this.likes += 1;
        this.user.increasePoint(PointPolicy.GET_LIKE);
    }

    public void decreaseLike() {
        if (this.likes <= 0) {
            this.likes = 0;
        }else{
            this.likes -= 1;
        }
        this.user.decreasePoint(PointPolicy.CANCEL_LIKE);
    }

    public void increaseDislike() {
        this.dislikes += 1;
        this.user.decreasePoint(PointPolicy.GET_DISLIKE);
    }

    public void decreaseDislike() {
        if (this.dislikes <= 0) {
            this.dislikes = 0;
        }else{
            this.dislikes -= 1;
        }
        this.user.increasePoint(PointPolicy.CANCEL_DISLIKE);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postNo=" + postNo +
                ", boardType=" + boardType +
                ", title='" + title + '\'' +
                ", passwd='" + passwd + '\'' +
                ", cont='" + cont + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", views=" + views +
                ", user=" + user +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }

}
