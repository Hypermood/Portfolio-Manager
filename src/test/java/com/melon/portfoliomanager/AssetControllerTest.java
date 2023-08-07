package com.melon.portfoliomanager;


import com.melon.portfoliomanager.dtos.TransactionDto;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.PortfolioItemRepository;
import com.melon.portfoliomanager.repositories.TransactionRepository;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
public class AssetControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private PortfolioItemRepository portfolioItemRepository;

    private TransactionDto transactionDto;


    @Test
    void buyAssets_NoSuchUser_ShouldReturnNoContent() {


        TransactionDto transactionDto = new TransactionDto("vvp","IBM",5.5,223.1);

        webTestClient.post().uri("/buy/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transactionDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("There is no user with such username. Enter a valid username.");
    }



    @Test
    void buyAssets_ValidRequest_ShouldReturnNoContent() {

        when(userRepository.findByUsername(any(String.class))).thenReturn((List.of(new User("vvp", "vpavlov@melon.com", "Georgi", "Ivanov"))));

        TransactionDto transactionDto = new TransactionDto("vvp","IBM",5.5,223.1);


        webTestClient.post().uri("/buy/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transactionDto))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void buyAssets_InvalidQuantity_ShouldBadRequest() {

        when(userRepository.findByUsername(any(String.class))).thenReturn((List.of(new User("vvp", "vpavlov@melon.com", "Georgi", "Ivanov"))));

        TransactionDto transactionDto = new TransactionDto("vvp","IBM",-5.5,223.1);


        webTestClient.post().uri("/buy/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transactionDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("[\"must be greater than or equal to 0.01\"]");
    }

    @Test
    void buyAssets_InvalidPrice_ShouldBadRequest() {

        when(userRepository.findByUsername(any(String.class))).thenReturn((List.of(new User("vvp", "vpavlov@melon.com", "Georgi", "Ivanov"))));

        TransactionDto transactionDto = new TransactionDto("vvp","IBM",5.5,-223.1);


        webTestClient.post().uri("/buy/asset")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transactionDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("[\"The price must be a positive number.\"]");
    }





}
