package com.melon.portfoliomanager;

import com.melon.portfoliomanager.dtos.UserDeleteDto;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.reactive.function.BodyInserters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    private UserDto validUserDto;

    @BeforeEach
    void setUp() {
        validUserDto = new UserDto();
        validUserDto.setUsername("vvp");
        validUserDto.setEmail("vpavlov@melon.com");
        validUserDto.setFirstName("Georgi");
        validUserDto.setLastName("Ivanov");
    }

    @Test
    void createUser_ValidRequest_ShouldReturnCreated() {

        when(userRepository.save(any(User.class))).thenReturn(new User("vvp", "vpavlov@melon.com", "Georgi", "Ivanov"));

        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(validUserDto))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void createUser_InvalidRequest_WrongEmail_ShouldReturnBadRequest() {
        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto("georgi", null, "v", "p")))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createUser_InvalidRequest_WrongUsername_ShouldReturnBadRequest() {
        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto(null, "", "v", "p")))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void createUser_UsernameAlreadyTaken_ShouldReturnBadRequest() {

        UserDto userDto = new UserDto("username", "email@example.com", "v", "p");
        when(userRepository.findAll()).thenReturn(List.of(new User("username", "email@example.com", "v", "p")));


        webTestClient.post()
                .uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void deleteUser_availableUser_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto("username");
        when(userRepository.findUsersByUsername(userDeleteDto.getUsername())).thenReturn(List.of(new User("username", "email@example.com", "v", "p")));


        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isNoContent();

    }

    @Test
    public void deleteUser_nullFieldRequest_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto();
//        when(userRepository.findUsersByUsername(userDeleteDto.getUsername())).thenReturn(List.of(new User("username", "email@example.com","v","p")));


        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void deleteUser_nonExistentUser_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto("username");
        when(userRepository.findUsersByUsername(userDeleteDto.getUsername())).thenReturn(new ArrayList<>());


        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isBadRequest();
    }

}