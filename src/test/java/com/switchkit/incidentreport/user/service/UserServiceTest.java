package com.switchkit.incidentreport.user.service;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.domain.model.CreateUserModel;
import com.switchkit.incidentreport.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    AppUser user;
    @BeforeEach
    void setUp() {
                 user = AppUser.builder()
                .username("boffin")
                .name("John Doe")
                .password("password123")
                .isActive(true)
                .createAt(LocalDateTime.now())
                .build();



    }

    @Test
    void WhenPassedValidUserObject_ShouldReturnCreatedCode() {


        Mockito.when(userRepository.existsByUsername("boffin")).thenReturn(false);
        AppUser user2 = AppUser.builder()
                .username("boffin1")
                .name("John Doe")
                .password("password123")
                .isActive(true)
                .createAt(LocalDateTime.now())
                .build();
        Mockito.when(userRepository.save(user)).thenReturn(user2);
        CreateUserModel model = CreateUserModel
                .builder()
                .name("John Doe")
                .username("boffin1")
                .name("John Doe")
                .password("password123")
                .build();

        ResponseEntity<ApiResponse> response = userService.createUser(model);


        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getMessage()).isEqualTo("created successfully");
    }
}