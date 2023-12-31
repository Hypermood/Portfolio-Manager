package com.melon.portfoliomanager.controllers;

import com.melon.portfoliomanager.dtos.responses.AnalyticsResponseDto;
import com.melon.portfoliomanager.exceptions.AnalyticsException;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.services.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private final static Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<?> getAnalytics(@RequestParam(value = "username") String username) {

        try {
            AnalyticsResponseDto response = analyticsService.fetchAnalyticsForUser(username);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NoSuchUserException | AnalyticsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(String.format("Unexpected error occurred during fetching information! exception=%s", e));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
