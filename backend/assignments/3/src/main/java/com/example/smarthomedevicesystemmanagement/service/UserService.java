package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.dto.LoginRequest;
import com.example.smarthomedevicesystemmanagement.dto.UserRequest;
import com.example.smarthomedevicesystemmanagement.entity.House;
import com.example.smarthomedevicesystemmanagement.entity.HouseMember;
import com.example.smarthomedevicesystemmanagement.entity.HouseRole;
import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.HouseMemberRepository;
import com.example.smarthomedevicesystemmanagement.repository.HouseRepository;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HouseRepository houseRepository;
    private final HouseMemberRepository houseMemberRepository;

    @Transactional
    public User addUser(UserRequest req, UUID houseId) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("addUser requested houseId={} newUserName={} by user={}", houseId, req.getUserName(), currentUsername);

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> {
                    log.warn("addUser failed: house not found houseId={}", houseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
                });

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("addUser failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("addUser failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        boolean isAdmin = houseMemberRepository
                .existsByHouse_HouseIdAndUser_UserIdAndRoleAndDeletedAtIsNull(houseId, currentUser.getUserId(), HouseRole.ADMIN);

        if (!isAdmin) {
            log.warn("addUser forbidden: not admin houseId={} user={}", houseId, currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        User user = new User();
        user.setUserName(req.getUserName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(null);
        user.setDeletedAt(null);

        User savedUser = userRepository.save(user);

        HouseMember hm = new HouseMember();
        hm.setCreatedAt(LocalDateTime.now());
        hm.setModifiedAt(null);
        hm.setDeletedAt(null);
        hm.setHouse(house);
        hm.setUser(savedUser);
        hm.setRole(HouseRole.USER);

        houseMemberRepository.save(hm);

        log.info("addUser success newUserId={} houseId={} by admin={}",
                savedUser.getUserId(), houseId, currentUsername);

        return savedUser;
    }

    public Map<String, Object> register(LoginRequest req) {

        log.info("register requested userName={}", req.getName());

        if (req.getName() == null || req.getName().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            log.warn("register failed: missing username/password");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        if (userRepository.findByUserNameAndDeletedAtIsNull(req.getName()).isPresent()) {
            log.warn("register failed: username already exists userName={}", req.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUserName(req.getName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(null);
        user.setDeletedAt(null);

        User saved = userRepository.save(user);

        log.info("register success userId={} userName={}", saved.getUserId(), saved.getUserName());

        return Map.of(
                "userId", saved.getUserId(),
                "userName", saved.getUserName(),
                "createdAt", saved.getCreatedAt()
        );
    }

    @Transactional
    public User tranferOwnership(UUID newOwnerUserId, UUID houseId) {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("transferOwnership requested houseId={} newOwnerUserId={} by user={}",
                houseId, newOwnerUserId, currentUsername);

        House house = houseRepository.findByHouseIdAndDeletedAtIsNull(houseId)
                .orElseThrow(() -> {
                    log.warn("transferOwnership failed: house not found houseId={}", houseId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "House not found");
                });

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.warn("transferOwnership failed: authentication missing houseId={}", houseId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User currentUser = userRepository.findByUserNameAndDeletedAtIsNull(currentUsername)
                .orElseThrow(() -> {
                    log.warn("transferOwnership failed: user not found user={}", currentUsername);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        HouseMember currentAdminMembership =houseMemberRepository
                .findByHouse_HouseIdAndUser_UserId(houseId, currentUser.getUserId())
                .orElseThrow(() -> {
                    log.warn("transferOwnership forbidden: current user not member houseId={} user={}", houseId, currentUsername);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this house");
                });

        if (currentAdminMembership.getRole() != HouseRole.ADMIN) {
            log.warn("transferOwnership forbidden: not admin houseId={} user={}", houseId, currentUsername);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the Admin of this house");
        }

        User newOwner = userRepository.findById(newOwnerUserId)
                .orElseThrow(() -> {
                    log.warn("transferOwnership failed: target user not found newOwnerUserId={}", newOwnerUserId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found");
                });

        HouseMember newOwnerMembership = houseMemberRepository
                .findByHouse_HouseIdAndUser_UserId(houseId, newOwnerUserId)
                .orElseThrow(() -> {
                    log.warn("transferOwnership bad request: target user not member houseId={} newOwnerUserId={}", houseId, newOwnerUserId);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target user is not a member of this house");
                });

        if (newOwnerMembership.getRole() == HouseRole.ADMIN) {
            log.info("transferOwnership no-op: target already admin houseId={} newOwnerUserId={}", houseId, newOwnerUserId);
            return newOwner;
        }

        currentAdminMembership.setRole(HouseRole.USER);
        currentAdminMembership.setModifiedAt(LocalDateTime.now());

        newOwnerMembership.setRole(HouseRole.ADMIN);
        newOwnerMembership.setModifiedAt(LocalDateTime.now());

        houseMemberRepository.save(currentAdminMembership);
        houseMemberRepository.save(newOwnerMembership);

        log.info("transferOwnership success houseId={} newAdminUserId={} oldAdminUser={}",
                house.getHouseId(), newOwnerUserId, currentUsername);

        return newOwner;
    }
}
