package com.hyunbenny.okkydokky.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "PARENT_NO")
    private Comments parentComm;

    @OneToMany(mappedBy = "parentComm", orphanRemoval = true)
    private List<Comments> childComm = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private Users regUser;

    @Column(name = "REG_DATE")
    private LocalDateTime regDate;


    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;

    @Builder
    public Comments(Long commentNo, Post post, FileInfo fileInfo, String comment, int likes, int dislikes, Comments parentComm, List<Comments> childComm, Users regUser, LocalDateTime regDate, LocalDateTime updDate) {
        this.commentNo = commentNo;
        this.post = post;
        this.fileInfo = fileInfo;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.parentComm = parentComm;
        this.childComm = childComm;
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
                ", parentComm=" + parentComm +
                ", childComm=" + childComm +
                ", regUser=" + regUser +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
