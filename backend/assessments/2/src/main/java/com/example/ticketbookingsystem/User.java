package com.example.ticketbookingsystem;
import java.util.List;

public class User {
    private String userName;
    private String password; // Must be stored securely!
    private String email;
    private List<String> roles; // e.g., "ROLE_USER", "ROLE_ADMIN"

    // Getter & Setter for userName
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter & Setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter & Setter for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter & Setter for roles
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

