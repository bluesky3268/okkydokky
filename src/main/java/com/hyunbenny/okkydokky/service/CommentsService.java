package com.hyunbenny.okkydokky.service;

import com.hyunbenny.okkydokky.common.util.Pager;
import com.hyunbenny.okkydokky.dto.comment.respDto.CommentListRespDto;
import com.hyunbenny.okkydokky.dto.comment.respDto.CommentRespDto;
import com.hyunbenny.okkydokky.entity.Comments;
import com.hyunbenny.okkydokky.repository.comments.CommentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;


//    public Page<CommentListRespDto> getCommentList(Long postNo, Pager pager) {
    public List<CommentListRespDto> getCommentList(Long postNo, Pager pager) {

        List<CommentListRespDto> comments = commentsRepository.findAllByPostNo(postNo, pager.of("COMMENT_NO")).stream().map(comment -> new CommentListRespDto().toCommentListRespDto(comment)).collect(Collectors.toList());
        List<CommentRespDto> childList = commentsRepository.findAllChildComments(postNo).stream().map(child -> new CommentRespDto().toCommentRespDto(child)).collect(Collectors.toList());

        for (CommentListRespDto comment : comments) {
            for (CommentRespDto child : childList) {
                if (comment.getCommentNo() == child.getParentNo()) {
                    comment.addChild(child);
                }
            }
        }
        return comments;
    }
}
