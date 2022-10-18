package com.hyunbenny.okkydokky.repository.comments;

import com.hyunbenny.okkydokky.entity.Comments;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentsRepositoryCustom {

    List<Comments> findAllByPostNo(Long postNo, Pageable pageable);

    List<Comments> findAllChildComments(Long postNo);
}
