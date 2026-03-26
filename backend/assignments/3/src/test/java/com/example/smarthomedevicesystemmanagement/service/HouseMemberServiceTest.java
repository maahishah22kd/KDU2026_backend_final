package com.example.smarthomedevicesystemmanagement.service;

import com.example.smarthomedevicesystemmanagement.entity.User;
import com.example.smarthomedevicesystemmanagement.repository.HouseRepository;
import com.example.smarthomedevicesystemmanagement.repository.UserRepository;
import com.example.smarthomedevicesystemmanagement.repository.HouseMemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class HouseMemberServiceTest {

    @Mock private HouseMemberRepository houseMemberRepository;
    @Mock private UserRepository userRepository;
    @Mock private HouseRepository houseRepository;

    @InjectMocks private HouseMemberService service;

    @BeforeEach
    void setupAuth() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("maahi", null, java.util.List.of()));
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAllHouses_success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);
        user.setUserName("maahi");

        when(userRepository.findByUserNameAndDeletedAtIsNull("maahi")).thenReturn(Optional.of(user));
        when(houseRepository.findAllByMemberUserId(eq(userId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(java.util.List.of()));

        var page = service.getAllHouses(0, 10);

        assertNotNull(page);
        verify(houseRepository).findAllByMemberUserId(eq(userId), any(Pageable.class));
    }
}
