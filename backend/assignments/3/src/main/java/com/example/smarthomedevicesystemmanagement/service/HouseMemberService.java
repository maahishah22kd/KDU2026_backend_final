package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.HouseMemberRepository;
import com.example.smarthomedevicesystemmanagement.repository.HouseRepository;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Setter
@Getter
@AllArgsConstructor
public class HouseMemberService {

    private final HouseMemberRepository houseMemberRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    public Page<House> getAllHouses(int page, int size) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("getAllHouses failed: authentication missing");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String userName = auth.getName();
        log.info("getAllHouses requested page={} size={} by user={}", page, size, userName);

        User user = userRepository.findByUserNameAndDeletedAtIsNull(userName)
                .orElseThrow(() -> {
                    log.warn("getAllHouses failed: user not found user={}", userName);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        Pageable pageable = PageRequest.of(page, size);
        return houseRepository.findAllByMemberUserId(user.getUserId(), pageable);
    }
}
