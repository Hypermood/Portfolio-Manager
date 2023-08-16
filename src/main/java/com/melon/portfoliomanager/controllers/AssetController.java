package com.melon.portfoliomanager.controllers;


import com.melon.portfoliomanager.dtos.TransactionDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.exceptions.NotEnoughStocksToSell;
import com.melon.portfoliomanager.services.AssetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

    private AssetService assetService;

    private final static Logger logger = LoggerFactory.getLogger(AssetController.class);

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
            logger.error(String.format("Unexpected error occurred during buying stocks ! exception=%s", e));
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/sell/asset")
    public ResponseEntity<String> sellAssets(@Valid @RequestBody TransactionDto transactionDto) {

        try {
            assetService.sellStock(transactionDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughStocksToSell e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(String.format("Unexpected error occurred during selling stocks ! exception=%s", e));
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
