package com.hyunbenny.okkydokky.repository.post;

import com.hyunbenny.okkydokky.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Post findByTitle(String title);
}
