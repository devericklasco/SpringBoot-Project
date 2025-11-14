package com.mtn.uganda.interview.interview.service;

import com.mtn.uganda.interview.interview.entity.Post;
import com.mtn.uganda.interview.interview.repository.PostRepository;
import com.mtn.uganda.interview.interview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MockDataService mockDataService;

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }
    public Post createPost(Post post) {
        validateUser(post.getUserId());
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post post) {
        Post existingPost = getPostById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setBody(post.getBody());
        existingPost.setIsPublished(post.getIsPublished());
        return postRepository.save(existingPost);
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    public Page<Post> getPostsByUserId(Long userId, Pageable pageable) {
        validateUser(userId);
        return postRepository.findByUserId(userId, pageable);
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    public List<Post> loadPostsFromMockData() {
        List<Post> posts = mockDataService.loadPosts();
        List<Post> savedPosts = new ArrayList<>();
        for (Post post : posts) {
            if (!postRepository.existsByTitleAndUserId(post.getTitle(), post.getUserId())) {
                post.setCreatedAt(LocalDateTime.now());
                post.setUpdatedAt(LocalDateTime.now());
                savedPosts.add(postRepository.save(post));
            }
        }
        log.info("Saved {} new posts to the database", savedPosts.size());
        return savedPosts;
    }

}

