package com.hyunbenny.okkydokky.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comments {

    @Id
    @Column(name = "COMMENT_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "FILE_NO")
    private FileInfo fileInfo;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "LIKES")
    private int likes;

    @Column(name = "DISLIKES")
    private int dislikes;

    private String regUser;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    @Builder
    public Comments(Long commentNo, Post post, FileInfo fileInfo, String comment, int likes, int dislikes, String regUser, LocalDateTime regDate, LocalDateTime updDate) {
        this.commentNo = commentNo;
        this.post = post;
        this.fileInfo = fileInfo;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.regUser = regUser;
        this.regDate = regDate;
        this.updDate = updDate;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "commentNo=" + commentNo +
                ", post=" + post +
                ", fileInfo=" + fileInfo +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", regUser='" + regUser + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
