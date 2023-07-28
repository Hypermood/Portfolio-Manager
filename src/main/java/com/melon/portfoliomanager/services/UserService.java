package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.exceptions.NullFieldException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService is a service class responsible for user-related operations.
 * The class uses UserRepository to interact with the database.
 */
@Service
public class UserService {

    // An instance of UserRepository to interact with the database.
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * It takes a UserRepository object as a parameter, which is automatically injected by Spring.
     *
     * @param userRepository An instance of UserRepository.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * The createUser method takes a User object and tries to create a new user in the database.
     * Before saving the user, it checks if the username is already used and if any required field is null.
     * If the username is already used, it throws a UsernameAlreadyUsedException.
     * If any required field is null, it throws a NullFieldException.
     * If none of the above exceptions occur, it saves the user in the database using UserRepository's save method.
     * If any other error occurs, it throws an Exception.
     *
     * @param user The User object to be created in the database.
     * @return The User object that was created in the database.
     * @throws Exception If any error occurs during the creation of the user.
     */
    public User createUser(User user) throws Exception {
        if(user.getUsername() == null){
            throw new NullFieldException("Invalid user input. The username is null.");
        }
        if(user.getEmail() == null){
            throw new NullFieldException("Invalid user input. The email is null.");
        }

        if(userRepository.findAll().stream().anyMatch(x->x.getUsername().equals(user.getUsername()))){
            throw new UsernameAlreadyUsedException(user.getUsername());
        }
        return userRepository.save(user);
    }




}
