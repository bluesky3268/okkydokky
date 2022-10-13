package com.hyunbenny.okkydokky.post.repository;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom{
    Page<Post> findAllPostsWithPaging(BoardType boardType, Pageable pageable);
}
