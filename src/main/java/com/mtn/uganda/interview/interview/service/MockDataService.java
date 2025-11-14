package com.mtn.uganda.interview.interview.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtn.uganda.interview.interview.config.AppConfiguration;
import com.mtn.uganda.interview.interview.entity.User;
import com.mtn.uganda.interview.interview.entity.Post;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MockDataService {
    private final AppConfiguration config;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public List<User> loadUsers(){
        try(InputStream inputStream = resourceLoader.getResource(config.getData().getUsersFile()).getInputStream()){
            List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
            log.info("Loaded {} users from mock data", users.size());
            return users;
        }
        catch (IOException e){
            throw new RuntimeException("Failed to load users", e);
        }
    }

    public List<Post> loadPosts() {
        try(InputStream inputStream = resourceLoader.getResource(config.getData().getPostsFile()).getInputStream()){
            List<Post> posts = objectMapper.readValue(inputStream, new TypeReference<List<Post>>() {});
            log.info("Loaded {} posts from mock data", posts.size());
            return posts;
        }
        catch (IOException e){
            throw new RuntimeException("Failed to load posts", e);
        }
    }



}
