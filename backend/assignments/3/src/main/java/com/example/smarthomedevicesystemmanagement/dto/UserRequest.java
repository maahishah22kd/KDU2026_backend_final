package com.example.smarthomedevicesystemmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "userName is required")
    @Size(min = 3, max = 50, message = "userName must be 3 to 50 characters")
    private String userName;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 100, message = "password must be 6 to 100 characters")
    private String password;
}
