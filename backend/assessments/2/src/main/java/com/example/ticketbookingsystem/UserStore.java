package com.example.ticketbookingsystem;
import com.example.ticketbookingsystem.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStore {

    private final Map<String, User> users = new HashMap<>();

    public UserStore(PasswordEncoder encoder) {

        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword(encoder.encode("admin123"));
        admin.setRoles(List.of("ROLE_ADMIN"));

        User basic = new User();
        basic.setUserName("user");
        basic.setPassword(encoder.encode("user123"));
        basic.setRoles(List.of("ROLE_USER"));

        users.put(admin.getUserName(), admin);
        users.put(basic.getUserName(), basic);
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void save(User user) {
        users.put(user.getUserName(), user);
    }
}
