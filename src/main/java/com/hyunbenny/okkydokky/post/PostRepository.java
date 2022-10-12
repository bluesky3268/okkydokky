package com.hyunbenny.okkydokky.post;

import com.hyunbenny.okkydokky.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
}
