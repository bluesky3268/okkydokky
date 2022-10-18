package com.hyunbenny.okkydokky.dto.comment.respDto;

import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.entity.FileInfo;
import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentListRespDto {

    private Long commentNo;

    private Long postNo;

    private FileInfo fileInfo;

    private String comment;

    private int likes;

    private int dislikes;

    private Long parentNo;

    private List<CommentRespDto> child = new ArrayList<>();

    private String regUserId;

    private LocalDateTime regDate;

    private LocalDateTime updDate;

    public void addChild(CommentRespDto commentRespDto) {
        this.child.add(commentRespDto);
    }

    public CommentListRespDto toCommentListRespDto(Comments comments) {
        this.commentNo = comments.getCommentNo();
        this.postNo = comments.getPost().getPostNo();
        this.fileInfo = comments.getFileInfo();
        this.comment = comments.getComment();
        this.likes = comments.getLikes();
        this.dislikes = comments.getDislikes();
        this.parentNo = comments.getParentComm() == null ? null : comments.getParentComm().getCommentNo();
        this.regUserId = comments.getRegUser().getUserId();
        this.regDate = comments.getRegDate();
        this.updDate = comments.getUpdDate();
        return this;
    }

    @Builder
    public CommentListRespDto(Long commentNo, Long postNo, FileInfo fileInfo, String comment, int likes, int dislikes, Long parentNo, List<CommentRespDto> child, String regUserId, LocalDateTime regDate, LocalDateTime updDate) {
        this.commentNo = commentNo;
        this.postNo = postNo;
        this.fileInfo = fileInfo;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.parentNo = parentNo;
        this.child = child;
        this.regUserId = regUserId;
        this.regDate = regDate;
        this.updDate = updDate;
    }

    @Override
    public String toString() {
        return "CommentListRespDto{" +
                "commentNo=" + commentNo +
                ", postNo=" + postNo +
                ", fileInfo=" + fileInfo +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", parentNo=" + parentNo +
                ", child=" + child +
                ", regUserId='" + regUserId + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
