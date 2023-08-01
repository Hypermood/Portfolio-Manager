package com.melon.portfoliomanager.controllers;


import com.melon.portfoliomanager.dtos.UserDeleteDto;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) throws Exception {

        try {
            User dtoToUser = userService.transformDto(userDto);
            userService.createUser(dtoToUser);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (UsernameAlreadyUsedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserDeleteDto userDeleteDto) throws Exception {

        try {
            userService.deleteUser(userDeleteDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (NoSuchUserException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.append(errorMessage).append("\n");
        });

        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
    }

}
