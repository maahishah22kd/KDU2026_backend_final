package com.example.jpa.Service;

import com.example.jpa.entity.User;
import com.example.jpa.InvalidPageSizeException;
import com.example.jpa.Repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Page<User> getUsers(int page, int size) {

        if (size < 1 || size > 50) {
            throw new InvalidPageSizeException(
                    "Page size must be between 1 and 50"
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
}
