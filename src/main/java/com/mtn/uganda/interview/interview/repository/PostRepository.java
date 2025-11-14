package com.mtn.uganda.interview.interview.repository;

import com.mtn.uganda.interview.interview.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserId(Long userId, Pageable pageable);
    List<Post> findByUserId(Long userId);
    Page<Post> findByIsPublishedTrue(Pageable pageable);
    boolean existsByTitleAndUserId(String title, Long userId);
}
