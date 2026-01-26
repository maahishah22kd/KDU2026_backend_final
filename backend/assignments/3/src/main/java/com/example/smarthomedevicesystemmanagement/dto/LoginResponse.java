package com.example.smarthomedevicesystemmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    private String accessToken;
    private String name;
    private List<String> role;
}
