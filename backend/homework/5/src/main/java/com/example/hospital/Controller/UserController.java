package com.example.hospital.Controller;

import com.example.hospital.Repository.UserJdbcRepository;
import com.example.hospital.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserJdbcRepository repo;

    public UserController(UserJdbcRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public UUID save(@RequestBody UserRequest req) {
        return repo.save(req.username(), req.timezone(), req.tenantId());
    }

    @GetMapping
    public Map<String, Object> list(
            @RequestParam UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return repo.findByTenant(tenantId, page, size);
    }
}
