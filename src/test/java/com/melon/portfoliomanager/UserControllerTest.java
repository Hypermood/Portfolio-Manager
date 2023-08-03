package com.melon.portfoliomanager;

import com.melon.portfoliomanager.dtos.UserDeleteDto;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
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
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("[\"Invalid user input. The email is null.\"]");
    }

    @Test
    void createUser_InvalidRequest_WrongUsername_ShouldReturnBadRequest() {
        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto(null, "", "v", "p")))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("[\"Invalid user input. The username is null.\"]");
    }

    @Test
    public void createUser_UsernameAlreadyTaken_ShouldReturnBadRequest() {

        UserDto userDto = new UserDto("username", "email@example.com", "v", "p");
        when(userRepository.findByUsername("username")).thenReturn(List.of(new User("username", "email@example.com", "v", "p")));


        webTestClient.post()
                .uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Username 'username' is already in use");
    }

    @Test
    public void deleteUser_availableUser_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto("username");
        when(userRepository.deleteByUsername(userDeleteDto.getUsername())).thenReturn(1L);

        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isNoContent();

    }

    @Test
    public void deleteUser_nullFieldRequest_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto();

        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("[\"The specified username can't be null when requesting to delete a user.\"]");
    }

    @Test
    public void deleteUser_nonExistentUser_ShouldReturnNoContent() {

        UserDeleteDto userDeleteDto = new UserDeleteDto("username");
        when(userRepository.deleteByUsername(userDeleteDto.getUsername())).thenThrow(new NoSuchUserException("The specified username can't be null when requesting to delete a user."));

        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("The specified username can't be null when requesting to delete a user.");
    }

    @Test
    public void createUser_InternalServerError(CapturedOutput capturedOutput) {

        UserDto userDto = new UserDto("username", "email@example.com", "v", "p");
        doThrow(new RuntimeException("Unexpected error")).when(userRepository).findByUsername(userDto.getUsername());

        webTestClient.post()
                .uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("Internal Server Error.");

        assertTrue(capturedOutput.getOut().contains("Unexpected error"));
    }

    @Test
    public void deleteUser_InternalServerError(CapturedOutput capturedOutput) {

        UserDeleteDto userDeleteDto = new UserDeleteDto("username");
        doThrow(new RuntimeException("Unexpected error")).when(userRepository).deleteByUsername(userDeleteDto.getUsername());

        webTestClient.method(HttpMethod.DELETE)
                .uri("/users/delete")
                .body(BodyInserters.fromValue(userDeleteDto))
                .exchange().expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("Internal Server Error.");

        assertTrue(capturedOutput.getOut().contains("Unexpected error"));
    }
}
