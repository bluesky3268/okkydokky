package com.hyunbenny.okkydokky.entity;

import com.hyunbenny.okkydokky.enums.PostType;
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
    @Column(name = "POST_NO")
    private Long postNo;

    @NotNull
    @Column(name = "POST_TYPE")
    private PostType postType;

    @NotNull
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Column(name = "PASSWD")
    private String passwd;

    @NotNull
    @Column(name = "CONT")
    private String cont;

    private int recommend;

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
    public Post(Long postNo, PostType postType, String title, String passwd, String cont, int recommend, long views, Users user, LocalDateTime regDate, LocalDateTime updDate) {
        this.postNo = postNo;
        this.postType = postType;
        this.title = title;
        this.passwd = passwd;
        this.cont = cont;
        this.recommend = recommend;
        this.views = views;
        this.user = user;
        this.regDate = regDate;
        this.updDate = updDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postNo=" + postNo +
                ", postType=" + postType +
                ", title='" + title + '\'' +
                ", passwd='" + passwd + '\'' +
                ", cont='" + cont + '\'' +
                ", recommend=" + recommend +
                ", views=" + views +
                ", user=" + user +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }

    public void updateTitle(String updateTitle) {
        this.title = updateTitle;
        this.updDate = LocalDateTime.now();
    }

    public void updateCont(String updateCont) {
        this.cont = updateCont;
        this.updDate = LocalDateTime.now();
    }
}
