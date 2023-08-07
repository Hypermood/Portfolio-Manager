package com.melon.portfoliomanager.controllers;


import com.melon.portfoliomanager.dtos.TransactionDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.services.AssetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class AssetController {

    private AssetService assetService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }


    @PostMapping("/buy/asset")
    public ResponseEntity<String> buyAssets(@Valid @RequestBody TransactionDto transactionDto) {

        try {
            assetService.buyStock(transactionDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(String.format("Unexpected error occurred during creating user ! exception=%s", e));
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();

        List<String> errorsMessages = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .toList();

        return new ResponseEntity<>(errorsMessages, HttpStatus.BAD_REQUEST);
    }

}
