package com.mtn.uganda.interview.interview.component;



import com.mtn.uganda.interview.interview.entity.Post;
import com.mtn.uganda.interview.interview.repository.PostRepository;
import com.mtn.uganda.interview.interview.repository.UserRepository;
import com.mtn.uganda.interview.interview.service.MockDataService;
import com.mtn.uganda.interview.interview.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
    private final UserService userService;
    private final MockDataService mockDataService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
        try {
            log.info("Loading initial data...");

            userService.loadUsersFromMockData();

            List<Post> posts = mockDataService.loadPosts();
            int savedCount = 0;

            for (Post post : posts) {
                if (userRepository.existsById(post.getUserId())) {
                    if (!postRepository.existsByTitleAndUserId(post.getTitle(), post.getUserId())) {
                        Post newPost = new Post();
                        newPost.setUserId(post.getUserId());
                        newPost.setTitle(post.getTitle());
                        newPost.setBody(post.getBody());
                        newPost.setIsPublished(false);
                        newPost.setCreatedAt(LocalDateTime.now());
                        newPost.setUpdatedAt(LocalDateTime.now());

                        postRepository.save(newPost);
                        savedCount++;
                    }
                } else {
                    log.warn("User with ID {} not found, skipping post: {}", post.getUserId(), post.getTitle());
                }
            }

            log.info("Initial data loaded successfully. Loaded {} users and {} posts",
                    userRepository.count(), savedCount);

        } catch (Exception e) {
            log.error("Error loading initial data: {}", e.getMessage(), e);
        }
    }
}
