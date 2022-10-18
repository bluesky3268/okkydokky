package com.hyunbenny.okkydokky.repository.comments;

import com.hyunbenny.okkydokky.entity.Comments;

import java.util.List;

public interface CommentsRepositoryCustom {

    List<Comments> findAllByPostNo(Long postNo);

    List<Comments> findAllChildComments(Long postNo);
}
