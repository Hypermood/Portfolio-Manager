package com.melon.portfoliomanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.portfoliomanager.controllers.UserController;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.exceptions.NullFieldException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import com.melon.portfoliomanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    private UserService userService;

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
    void createUser_ValidRequest_ShouldReturnCreated() throws Exception {

        when(userService.createUser(any(User.class))).thenReturn(new User("vvp","vpavlov@melon.com","Georgi","Ivanov"));

        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(validUserDto))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void createUser_InvalidRequest_WrongEmail_ShouldReturnBadRequest() throws JsonProcessingException {
        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto("georgi",null,"v","p")))
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void createUser_InvalidRequest_WrongUsername_ShouldReturnBadRequest() throws JsonProcessingException {
        webTestClient.post().uri("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto(null,"","v","p")))
                .exchange()
                .expectStatus().isBadRequest();
    }



}