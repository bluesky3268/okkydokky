package com.hyunbenny.okkydokky.post.repository;

import com.hyunbenny.okkydokky.entity.Post;
import com.hyunbenny.okkydokky.entity.QPost;
import com.hyunbenny.okkydokky.entity.QUsers;
import com.hyunbenny.okkydokky.enums.BoardType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Post findByTitle(String title);
}
