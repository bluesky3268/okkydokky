package com.hyunbenny.okkydokky.dto.comment.respDto;

import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.entity.FileInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentRespDto {

    private Long commentNo;

    private Long postNo;

    private FileInfo fileInfo;

    private String comment;

    private int likes;

    private int dislikes;

    private Long parentNo;

    private String regUserId;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    public CommentRespDto toCommentRespDto(Comments comments) {
        this.commentNo = comments.getCommentNo();
        this.postNo = comments.getPost().getPostNo();
        this.fileInfo = comments.getFileInfo();
        this.comment = comments.getComment();
        this.likes = comments.getLikes();
        this.dislikes = comments.getDislikes();
        this.parentNo = comments.getParentComm().getCommentNo();
        this.regUserId = comments.getRegUser().getUserId();
        this.regDate = comments.getRegDate();
        this.updDate = comments.getUpdDate();
        return this;
    }

    @Builder
    public CommentRespDto(Long commentNo, Long postNo, FileInfo fileInfo, String comment, int likes, int dislikes, Long parentNo, String regUserId, LocalDateTime regDate, LocalDateTime updDate) {
        this.commentNo = commentNo;
        this.postNo = postNo;
        this.fileInfo = fileInfo;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.parentNo = parentNo;
        this.regUserId = regUserId;
        this.regDate = regDate;
        this.updDate = updDate;
    }

    @Override
    public String toString() {
        return "CommentRespDto{" +
                "commentNo=" + commentNo +
                ", postNo=" + postNo +
                ", fileInfo=" + fileInfo +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", parentNo=" + parentNo +
                ", regUserId='" + regUserId + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
