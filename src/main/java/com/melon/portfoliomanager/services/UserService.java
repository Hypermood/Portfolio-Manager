package com.melon.portfoliomanager.services;

import com.melon.portfoliomanager.dtos.UserDeleteDto;
import com.melon.portfoliomanager.dtos.UserDto;
import com.melon.portfoliomanager.exceptions.NoSuchUserException;
import com.melon.portfoliomanager.exceptions.NullFieldException;
import com.melon.portfoliomanager.exceptions.UsernameAlreadyUsedException;
import com.melon.portfoliomanager.models.User;
import com.melon.portfoliomanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user) throws Exception {

        if(isUsernameTaken(user)){
            throw new UsernameAlreadyUsedException(user.getUsername());
        }
        return userRepository.save(user);
    }


    public User transformDto(UserDto userDto) {

        return new User(userDto.getUsername(), userDto.getEmail(), userDto.getFirstName(), userDto.getLastName());

    }

    public boolean isUsernameTaken(User user){
        return userRepository.findAll().stream().anyMatch(x->x.getUsername().equals(user.getUsername()));
    }

    public User deleteUser(UserDeleteDto userDeleteDto) {

        List<User> users = userRepository.findUsersByUsername(userDeleteDto.getUsername());

        if(users.isEmpty()){
            throw new NoSuchUserException("There is no such user.");
        }
        userRepository.delete(users.get(0));
        return users.get(0);

    }

}
