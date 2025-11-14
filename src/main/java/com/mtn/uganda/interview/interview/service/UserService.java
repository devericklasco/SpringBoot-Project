package com.mtn.uganda.interview.interview.service;


import com.mtn.uganda.interview.interview.entity.User;
import com.mtn.uganda.interview.interview.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final MockDataService mockDataService;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with id: " + id));
    }
    public List<User> loadUsersFromMockData() {
        List<User> users = mockDataService.loadUsers();
        List<User> savedUsers = new ArrayList<>();
        for (User user : users) {
            if(!userRepository.existsByUsername(user.getUsername())) {
                User newUser = new User();
                newUser.setName(user.getName());
                newUser.setUsername(user.getUsername());
                newUser.setEmail(user.getEmail());
                newUser.setPhone(user.getPhone());
                newUser.setWebsite(user.getWebsite());

                savedUsers.add(userRepository.save(newUser));
            }
        }
        log.info("Saved {} new users to the database", savedUsers.size());
        return userRepository.findAll();
    }



}
