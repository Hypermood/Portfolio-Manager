package com.melon.portfoliomanager.controllers;


import com.melon.portfoliomanager.exceptions.NullFieldException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * UserController is a REST controller that handles HTTP requests related to user management.
 * This includes creating a new user.
 * The controller is mapped to the "/users" URL.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * An instance of UserService to handle the business logic related to users.
     */
    private final UserService userService;

    /**
     * Constructor for UserController.
     * It takes a UserService object as a parameter, which is automatically injected by Spring.
     *
     * @param userService An instance of UserService.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * A POST endpoint that creates a new user.
     * The User object is automatically created from the JSON in the request body.
     * The User object is then passed to the UserService to perform the creation logic.
     * The endpoint returns a 201 CREATED status if the user was created successfully.
     * If the user's username is already in use or any required fields are null, it returns a 400 BAD REQUEST status with an error message.
     * For any other errors, it returns a 500 INTERNAL SERVER ERROR status.
     *
     * @param user The User object created from the JSON in the request body.
     * @return A ResponseEntity with either a 201, 400, or 500 status.
     */
    @PostMapping("/add")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UsernameAlreadyUsedException | NullFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
